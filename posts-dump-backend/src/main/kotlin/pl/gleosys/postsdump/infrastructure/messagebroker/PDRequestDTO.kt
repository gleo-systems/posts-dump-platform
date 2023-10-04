package pl.gleosys.postsdump.infrastructure.messagebroker

import com.squareup.moshi.JsonClass
import pl.gleosys.postsdump.domain.PostDumpEvent
import java.util.*

@JsonClass(generateAdapter = true)
data class PDRequestDTO(override val id: UUID) : Message

fun PDRequestDTO.toDomain() = PostDumpEvent(id = id)
