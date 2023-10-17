package pl.gleosys.postsdump.infrastructure.httpclient

import arrow.core.Either
import arrow.core.Either.Companion.catch
import arrow.core.Either.Left
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.FuelError
import com.github.kittinunf.result.Result
import com.github.kittinunf.result.map
import pl.gleosys.postsdump.application.ports.PostsAPIClient
import pl.gleosys.postsdump.domain.Post
import pl.gleosys.postsdump.infrastructure.JSONParser
import pl.gleosys.postsdump.util.Failure
import pl.gleosys.postsdump.util.Failure.FailureFactory
import pl.gleosys.postsdump.util.Failure.InfrastructureError

private class JSONPlaceholderAdapter

class JSONPlaceholderHTTPClient(parser: JSONParser) : PostsAPIClient {
    private val getPostsAdapter = parser.newListAdapter(PostDTO::class.java)

    // TODO: apply suspend
    override fun getPosts(): Either<Failure, List<Post>> {
        return catch {
            Fuel.request(JSONPlaceholderAPI.GetPosts())
                .responseObject<List<PostDTO>>(getPostsAdapter)
                .third
                .map { it.map(PostDTO::toDomain) }
        }
            .mapLeft<InfrastructureError>(FailureFactory::newInstance)
            .fold(::Left, Result<List<Post>, FuelError>::toEither)
    }
}
