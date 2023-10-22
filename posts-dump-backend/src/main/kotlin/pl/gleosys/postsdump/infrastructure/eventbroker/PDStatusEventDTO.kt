package pl.gleosys.postsdump.infrastructure.eventbroker

import arrow.core.Either
import arrow.core.Either.Companion.catch
import com.squareup.moshi.JsonClass
import pl.gleosys.postsdump.core.Failure
import pl.gleosys.postsdump.core.Failure.FailureFactory
import pl.gleosys.postsdump.core.Failure.ValidationError
import pl.gleosys.postsdump.domain.DumpStatus
import java.util.*

interface PDEvent {
    val id: UUID
}

enum class PDStatus(val value: String) {
    IN_PROGRESS("IN_PROGRESS"),
    COMPLETED("COMPLETED")
}

@JsonClass(generateAdapter = true)
data class PDStatusEventDTO(override val id: UUID, val status: PDStatus) : PDEvent {

    companion object {
        @JvmStatic
        fun of(id: UUID, status: DumpStatus): Either<Failure, PDStatusEventDTO> {
            return catch { id to PDStatus.valueOf(status.value) }
                .mapLeft<ValidationError>(FailureFactory::newInstance)
                .map { (id, status) -> PDStatusEventDTO(id, status) }
        }
    }
}
