package pl.gleosys.postsdump.infrastructure.storage

import arrow.core.Either
import arrow.core.Either.Companion.catch
import arrow.core.right
import pl.gleosys.postsdump.core.Failure
import pl.gleosys.postsdump.core.Success
import java.io.File
import java.nio.file.Path

class FileSystemService(private val properties: FileSystemProperties) {
    fun createFile(destination: Path): Either<Failure, File> {
        return catch {
            val location = properties.baseDirectory.resolve(destination).also {
                it.parent.toFile().mkdirs()
            }
            return location.toFile().apply {
                createNewFile()
            }.right()
        }
            .mapLeft<Failure.InfrastructureError>(Failure.FailureFactory::newInstance)
    }

    fun writeFile(data: Pair<File, ByteArray>): Either<Failure, Success> {
        return data.right()
            .onRight { (file, content) -> file.writeBytes(content) }
            .map { Success }
    }
}
