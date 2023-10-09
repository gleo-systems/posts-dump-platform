package pl.gleosys.postsdump.application.process

import arrow.core.None
import arrow.core.Option
import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.inject.Inject
import jakarta.inject.Named
import pl.gleosys.postsdump.application.aop.LogDuration
import pl.gleosys.postsdump.application.ports.PostsAPIClient
import pl.gleosys.postsdump.application.ports.StorageUploader
import pl.gleosys.postsdump.domain.Event
import pl.gleosys.postsdump.domain.Failure
import pl.gleosys.postsdump.domain.Post
import java.nio.file.Path as JPath

private val logger = KotlinLogging.logger {}

// Open class and @Inject are required for AOP :(
open class RunDumpProcess @Inject constructor(
    private val apiClient: PostsAPIClient,
    @Named("bucketsStorageUploader") private val storage: StorageUploader
) {
    @LogDuration
    open fun execute(event: Event): Option<Failure> {
        logger.info { "Starting new dump process for $event" }

        // TODO: non-blocking
        return apiClient.getPosts()
            .map {
                it.map { post -> Pair<JPath, Post>(Path.jsonExtOf(event, post), post) }
                    .map { pair -> storage.uploadFile(pair.first, pair.second, Post::class.java) }
            }
            .fold({ Option.invoke(it) }) { it.firstOrNull(Option<Failure>::isSome) ?: None }
            .onNone { logger.info { "Successfully completed dump process for $event" } }
    }
}
