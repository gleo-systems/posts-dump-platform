package pl.gleosys.postsdump.application.ports

import arrow.core.Either
import pl.gleosys.postsdump.domain.Post
import pl.gleosys.postsdump.util.Failure

interface PostsAPIClient {
    fun getPosts(): Either<Failure, List<Post>>
}
