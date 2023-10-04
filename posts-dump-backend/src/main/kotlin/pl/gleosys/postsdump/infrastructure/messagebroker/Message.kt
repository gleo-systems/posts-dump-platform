package pl.gleosys.postsdump.infrastructure.messagebroker

import java.util.*

interface Message {
    val id: UUID
}
