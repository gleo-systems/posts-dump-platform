package pl.gleosys.postsdump.infrastructure.storage

import arrow.core.Either
import pl.gleosys.postsdump.util.Failure
import pl.gleosys.postsdump.util.Success
import java.nio.file.Path

interface StorageClient {
    fun upload(destination: Path, content: ByteArray): Either<Failure, Success>
}
