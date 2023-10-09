package pl.gleosys.postsdump.infrastructure.httpclient

import arrow.core.Either
import arrow.core.Either.Companion.catch
import arrow.core.Either.Left
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.result.map
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import pl.gleosys.postsdump.application.ports.PostsAPIClient
import pl.gleosys.postsdump.domain.Failure
import pl.gleosys.postsdump.domain.Post
import pl.gleosys.postsdump.infrastructure.InfrastructureError

private class JSONPlaceholderAdapter

class JSONPlaceholderHTTPClient(mapper: Moshi) : PostsAPIClient {
    private val getPostsAdapter = mapper.adapter<List<PostDTO>>(
        Types.newParameterizedType(List::class.java, PostDTO::class.java)
    )

    // TODO: non-blocking
    override fun getPosts(): Either<Failure, List<Post>> {
        return catch {
            Fuel.request(JSONPlaceholderAPI.GetPosts())
                .responseObject<List<PostDTO>>(getPostsAdapter)
                .third
                .map { listDTO -> listDTO.map(PostDTO::toDomain) }
                .toEither()
        }
            .mapLeft(::InfrastructureError)
            .fold(::Left) { it }
    }
}
