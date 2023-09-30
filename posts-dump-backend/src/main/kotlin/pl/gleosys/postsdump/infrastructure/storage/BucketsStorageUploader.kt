package pl.gleosys.postsdump.infrastructure.storage

import io.github.oshai.kotlinlogging.KotlinLogging
import pl.gleosys.postsdump.application.ports.StorageUploader
import java.nio.file.Path

private val logger = KotlinLogging.logger {}

class BucketsStorageUploader(private val client: StorageClient) : StorageUploader {
    override fun uploadFile(destination: Path, content: ByteArray) {
        logger.debug { "Uploading content='${String(content)}' to destination=$destination" }
        client.upload(destination, content)
        logger.info { "Successfully uploaded data to destination=$destination" }
    }
}
