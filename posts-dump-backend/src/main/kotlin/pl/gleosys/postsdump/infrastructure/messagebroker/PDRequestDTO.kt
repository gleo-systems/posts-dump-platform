package pl.gleosys.postsdump.infrastructure.messagebroker

import com.squareup.moshi.JsonClass
import pl.gleosys.postsdump.domain.PostDumpEvent
import java.util.*

@JsonClass(generateAdapter = true)
open class PDRequestDTO(open val id: UUID) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is PDRequestDTO) return false

        return id == other.id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }

    override fun toString(): String {
        return "PDRequestDTO(id=$id)"
    }
}

fun PDRequestDTO.toDomain() = PostDumpEvent(id = id)
