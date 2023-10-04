package pl.gleosys.postsdump.infrastructure.storage

import arrow.core.Option
import pl.gleosys.postsdump.domain.Failure
import java.nio.file.Path

interface StorageClient {
    fun upload(destination: Path, content: ByteArray): Option<Failure>
}
