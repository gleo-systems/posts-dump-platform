package pl.gleosys.postsdump.infrastructure.messagebroker

import arrow.core.Either
import arrow.core.Either.Companion.catch
import arrow.core.flatMap
import arrow.core.raise.either
import arrow.core.raise.ensure
import com.squareup.moshi.JsonClass
import pl.gleosys.postsdump.core.Failure
import pl.gleosys.postsdump.core.Failure.FailureFactory
import pl.gleosys.postsdump.core.Failure.ValidationError
import pl.gleosys.postsdump.domain.DumpEvent
import pl.gleosys.postsdump.domain.StorageType
import java.util.*

@JsonClass(generateAdapter = true)
data class PDRequestDTO(override val id: UUID, val storageType: String) : Message

fun PDRequestDTO.toDomain(): Either<Failure, DumpEvent> {
    return catch { StorageType.valueOf(storageType) }
        .mapLeft<ValidationError>(FailureFactory::newInstance)
        .flatMap { type ->
            either {
                ensure(storageType.isNotBlank()) { ValidationError("Empty storageType value") }
                DumpEvent(id, type)
            }
        }
}
