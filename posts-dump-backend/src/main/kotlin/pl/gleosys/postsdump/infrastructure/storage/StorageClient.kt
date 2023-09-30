package pl.gleosys.postsdump.infrastructure.storage

import java.nio.file.Path

interface StorageClient {
    fun upload(destination: Path, content: ByteArray)
}
