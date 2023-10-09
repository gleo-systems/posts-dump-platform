package pl.gleosys.postsdump.infrastructure.storage

import arrow.core.Option
import com.squareup.moshi.Moshi
import io.github.oshai.kotlinlogging.KotlinLogging
import pl.gleosys.postsdump.application.ports.StorageUploader
import pl.gleosys.postsdump.domain.Failure
import java.nio.file.Path

private val logger = KotlinLogging.logger {}

class BucketsStorageUploader(private val parser: Moshi, private val client: StorageClient) : StorageUploader {

    // TODO: catch parsing exceptions
    override fun <T> uploadFile(destination: Path, content: T, clazz: Class<T>): Option<Failure> {
        logger.debug { "Uploading content $content to destination=$destination" }
        return client.upload(
            destination,
            parser.adapter(clazz).toJson(content).toByteArray()
        )
            .onNone { logger.info { "Successfully uploaded content to destination=$destination" } }
    }
}
