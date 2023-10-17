package pl.gleosys.postsdump.infrastructure.storage

import arrow.core.Either
import arrow.core.Either.Companion.catch
import pl.gleosys.postsdump.util.Failure
import pl.gleosys.postsdump.util.Failure.FailureFactory
import pl.gleosys.postsdump.util.Failure.InfrastructureError
import pl.gleosys.postsdump.util.Success
import software.amazon.awssdk.core.sync.RequestBody
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.PutObjectRequest
import java.nio.file.Path

class BucketsStorageClient(
    private val properties: StorageProperties,
    private val delegate: S3Client
) : StorageClient {
    // TODO: apply suspend
    override fun upload(destination: Path, content: ByteArray): Either<Failure, Success> {
        return catch {
            require(destination.toString().isNotBlank())
            require(content.isNotEmpty())
            delegate.putObject(
                PutObjectRequest.builder()
                    .bucket(properties.baseLocation)
                    .key(destination.toString())
                    .build(),
                RequestBody.fromBytes(content)
            )
        }
            .mapLeft<InfrastructureError>(FailureFactory::newInstance)
            .map { Success }
    }
}
