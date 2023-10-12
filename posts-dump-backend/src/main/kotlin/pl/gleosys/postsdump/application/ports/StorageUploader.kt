package pl.gleosys.postsdump.application.ports

import arrow.core.Either
import pl.gleosys.postsdump.core.Failure
import pl.gleosys.postsdump.core.Success
import java.nio.file.Path

interface StorageUploader {
    fun <T> uploadFile(destination: Path, content: T, clazz: Class<T>): Either<Failure, Success>
}
