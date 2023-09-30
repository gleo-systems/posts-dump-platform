package pl.gleosys.postsdump.application.ports

import java.nio.file.Path

interface StorageUploader {
    fun uploadFile(destination: Path, content: ByteArray)
}
