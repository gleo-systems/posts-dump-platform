package pl.gleosys.postsdump.application.process

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.right
import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.inject.Inject
import pl.gleosys.postsdump.application.ports.PostsAPIClient
import pl.gleosys.postsdump.application.ports.StorageUploader
import pl.gleosys.postsdump.core.Failure
import pl.gleosys.postsdump.core.Success
import pl.gleosys.postsdump.domain.DumpEvent
import pl.gleosys.postsdump.domain.Post

private val logger = KotlinLogging.logger {}

// TODO: use FlowKT and suspend
class RunDumpProcess @Inject constructor(
    private val apiClient: PostsAPIClient,
    private val storage: StorageUploader
) {
    fun execute(event: DumpEvent): Either<Failure, Success> {
        return event.right()
            .onRight { e -> logger.info { "Starting new dump process for $e" } }
            .flatMap { e ->
                apiClient.getPosts()
                    .map { list ->
                        list.map { post -> Pair(e, post) }
                            .map { (event, post) ->
                                Triple(Path.jsonExtOf(event, post), post, Post::class.java)
                            }
                            .map { (path, content, clazz) ->
                                storage.uploadFile(path, content, clazz)
                            }
                            .filter(Either<Failure, Success>::isLeft)
                    }
                    .flatMap(::getFirstErrorOrSuccess)
            }
            .onRight { logger.info { "Successfully completed dump process for $event" } }
    }

    private fun getFirstErrorOrSuccess(failures: List<Either<Failure, Success>>): Either<Failure, Success> =
        if (failures.isNotEmpty()) failures.first() else Success.right()
}
