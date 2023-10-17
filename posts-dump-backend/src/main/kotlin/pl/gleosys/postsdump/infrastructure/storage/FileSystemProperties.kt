package pl.gleosys.postsdump.infrastructure.storage

import arrow.core.Either
import arrow.core.Either.Companion.catch
import pl.gleosys.postsdump.core.Failure
import pl.gleosys.postsdump.core.Failure.ValidationError
import java.nio.file.InvalidPathException
import java.nio.file.Path
import kotlin.io.path.exists

data class FileSystemProperties private constructor(
    val baseDirectory: Path
) {
    companion object {
        private const val WORKING_DIRECTORY_PREFIX = ""
        operator fun invoke(
            baseDirectory: String
        ): Either<ValidationError, FileSystemProperties> {
            return catch { checkExists(baseDirectory) }
                .mapLeft<ValidationError>(Failure::newInstance)
                .map(::FileSystemProperties)
        }

        @Throws(InvalidPathException::class)
        private fun checkExists(directory: String): Path =
            Path.of(WORKING_DIRECTORY_PREFIX, directory).apply { exists() }
    }
}
