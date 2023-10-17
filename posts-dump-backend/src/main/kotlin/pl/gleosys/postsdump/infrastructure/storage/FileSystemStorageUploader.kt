package pl.gleosys.postsdump.infrastructure.storage

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.raise.either
import arrow.core.raise.ensure
import io.github.oshai.kotlinlogging.KotlinLogging
import pl.gleosys.postsdump.application.ports.StorageUploader
import pl.gleosys.postsdump.core.Failure
import pl.gleosys.postsdump.core.Failure.ValidationError
import pl.gleosys.postsdump.core.Success
import pl.gleosys.postsdump.infrastructure.JSONParser
import java.nio.file.Path

private val logger = KotlinLogging.logger {}

class FileSystemStorageUploader(
    private val parser: JSONParser,
    private val service: FileSystemService
) : StorageUploader {
    override fun <T> uploadFile(
        destination: Path,
        content: T,
        clazz: Class<T>
    ): Either<Failure, Success> {
        return either {
            ensure(
                destination.toString().isNotBlank()
            ) { ValidationError("Empty destination path") }

            Triple(destination, content, clazz)
        }
            .onRight { (destination, content, clazz) ->
                logger.debug { "Uploading content $content with class=${clazz.simpleName} to destination=$destination" }
            }
            .flatMap { (destination, content, clazz) ->
                parser.toByteArray(content, clazz).map { Pair(destination, it) }
            }
            .flatMap { (destination, content) ->
                service.createFile(destination).map { Pair(it, content) }
            }
            .flatMap(service::writeFile)
    }
}
