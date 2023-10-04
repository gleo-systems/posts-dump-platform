package pl.gleosys.postsdump.application.ports

import arrow.core.Either
import pl.gleosys.postsdump.domain.Failure
import pl.gleosys.postsdump.domain.Post

interface PostsAPIClient {
    fun getPosts(): Either<Failure, List<Post>>
}
