package pl.gleosys.postsdump.application.ports

import arrow.core.Option
import pl.gleosys.postsdump.domain.Failure
import java.nio.file.Path

interface StorageUploader {
    fun uploadFile(destination: Path, content: ByteArray): Option<Failure>
}
