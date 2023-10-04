package pl.gleosys.postsdump.application.ports

import arrow.core.Option
import pl.gleosys.postsdump.domain.Failure
import java.nio.file.Path

interface StorageUploader {
    fun <T> uploadFile(destination: Path, content: T, clazz: Class<T>): Option<Failure>
}
