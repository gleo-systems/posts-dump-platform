package pl.gleosys.postsdump.application.process

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.right
import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.inject.Inject
import jakarta.inject.Named
import pl.gleosys.postsdump.application.ports.PostsAPIClient
import pl.gleosys.postsdump.application.ports.StorageUploader
import pl.gleosys.postsdump.domain.Event
import pl.gleosys.postsdump.domain.Post
import pl.gleosys.postsdump.util.Failure
import pl.gleosys.postsdump.util.Success
import pl.gleosys.postsdump.util.aop.LogDuration
import java.nio.file.Path as PathJ

private val logger = KotlinLogging.logger {}

// Open class and @Inject are required for AOP :(
open class RunDumpProcess @Inject constructor(
    private val apiClient: PostsAPIClient,
    @Named("bucketsStorageUploader") private val storage: StorageUploader
) {

    // TODO: use FlowKT and suspend
    @LogDuration
    open fun execute(event: Event): Either<Failure, Success> {
        logger.info { "Starting new dump process for $event" }
        return apiClient.getPosts()
            .map { list ->
                list.map { post -> prepareUploadData(post, event) }
                    .map(::uploadFile)
                    .filter(Either<Failure, Success>::isLeft)
            }
            .flatMap(::getFirstErrorOrSuccess)
            .onRight { logger.info { "Successfully completed dump process for $event" } }
    }

    private fun prepareUploadData(post: Post, event: Event): Triple<PathJ, Post, Class<Post>> =
        Triple(Path.jsonExtOf(event, post), post, Post::class.java)

    private fun <T> uploadFile(data: Triple<PathJ, T, Class<T>>): Either<Failure, Success> =
        storage.uploadFile(data.first, data.second, data.third)

    private fun getFirstErrorOrSuccess(failures: List<Either<Failure, Success>>): Either<Failure, Success> =
        if (failures.isNotEmpty()) failures.first() else Success.right()
}
