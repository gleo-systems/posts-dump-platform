package pl.gleosys.postsdump.application.ports

import arrow.core.Either
import pl.gleosys.postsdump.util.Failure
import pl.gleosys.postsdump.util.Success
import java.nio.file.Path

interface StorageUploader {
    fun <T> uploadFile(destination: Path, content: T, clazz: Class<T>): Either<Failure, Success>
}
