package pl.gleosys.postsdump.infrastructure.storage

import arrow.core.Either.Companion.catch
import arrow.core.None
import arrow.core.Option
import pl.gleosys.postsdump.domain.Failure
import pl.gleosys.postsdump.infrastructure.InfrastructureError
import software.amazon.awssdk.core.sync.RequestBody
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.PutObjectRequest
import java.nio.file.Path

class BucketsStorageClient(
    private val properties: StorageProperties,
    private val delegate: S3Client,
) : StorageClient {
    override fun upload(destination: Path, content: ByteArray): Option<Failure> {
        return catch {
            delegate.putObject(
                PutObjectRequest.builder()
                    .bucket(properties.baseLocation)
                    .key(destination.toString())
                    .build(),
                RequestBody.fromBytes(content),
            )
        }
            .fold({ Option.invoke(InfrastructureError(cause = it)) }, { None })
    }
}
