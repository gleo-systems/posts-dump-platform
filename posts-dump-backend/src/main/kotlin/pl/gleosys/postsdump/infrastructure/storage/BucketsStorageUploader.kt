package pl.gleosys.postsdump.infrastructure.storage

import arrow.core.Option
import io.github.oshai.kotlinlogging.KotlinLogging
import pl.gleosys.postsdump.application.ports.StorageUploader
import pl.gleosys.postsdump.domain.Failure
import java.nio.file.Path

private val logger = KotlinLogging.logger {}

class BucketsStorageUploader(private val client: StorageClient) : StorageUploader {

    override fun uploadFile(destination: Path, content: ByteArray): Option<Failure> {
        logger.debug { "Uploading content='${String(content)}' to destination=$destination" }
        val failureOpt = client.upload(destination, content)
        logger.info { "Successfully uploaded data to destination=$destination" }
        return failureOpt
    }
}
