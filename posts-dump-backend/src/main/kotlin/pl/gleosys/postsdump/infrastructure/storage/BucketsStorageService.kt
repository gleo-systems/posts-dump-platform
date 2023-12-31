package pl.gleosys.postsdump.infrastructure.storage

import arrow.core.Either
import arrow.core.Either.Companion.catch
import pl.gleosys.postsdump.core.Failure
import pl.gleosys.postsdump.core.Failure.FailureFactory.newInstance
import pl.gleosys.postsdump.core.Failure.InfrastructureError
import pl.gleosys.postsdump.core.Success
import software.amazon.awssdk.core.sync.RequestBody
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.PutObjectRequest
import java.nio.file.Path

class BucketsStorageService(
    private val properties: StorageProperties,
    private val client: S3Client
) : StorageService {
    // TODO: apply suspend
    override fun uploadData(destination: Path, content: ByteArray): Either<Failure, Success> {
        return catch {
            require(destination.toString().isNotBlank())
            require(content.isNotEmpty())

            client.putObject(
                PutObjectRequest.builder()
                    .bucket(properties.baseLocation)
                    .key(destination.toString())
                    .build(),
                RequestBody.fromBytes(content)
            )
        }
            .mapLeft<InfrastructureError>(::newInstance)
            .map { Success }
    }
}
