package pl.gleosys.postsdump.infrastructure.storage

import arrow.core.Either
import arrow.core.raise.either
import arrow.core.raise.ensure
import pl.gleosys.postsdump.core.Failure.ValidationError

data class StorageProperties private constructor(
    val region: String,
    val accessKeyId: String,
    val secretAccessKey: String,
    val apiURL: String,
    val baseLocation: String
) {
    companion object {
        operator fun invoke(
            region: String,
            accessKeyId: String,
            secretAccessKey: String,
            apiURL: String,
            baseLocation: String
        ): Either<ValidationError, StorageProperties> {
            return either {
                ensure(region.isNotBlank()) { ValidationError("Empty region value") }
                ensure(accessKeyId.isNotBlank()) { ValidationError("Empty accessKeyId value") }
                ensure(secretAccessKey.isNotBlank()) { ValidationError("Empty secretAccessKey value") }
                ensure(apiURL.isNotBlank()) { ValidationError("Empty apiURL value") }
                ensure(baseLocation.isNotBlank()) { ValidationError("Empty baseLocation value") }
                StorageProperties(region, accessKeyId, secretAccessKey, apiURL, baseLocation)
            }
        }
    }

    override fun toString() =
        "StorageProperties(" +
            "region=$region, " +
            "accessKeyId=$accessKeyId, " +
            "secretAccessKey=###,  " +
            "apiURL=$apiURL, " +
            "baseLocation=$baseLocation)"
}
