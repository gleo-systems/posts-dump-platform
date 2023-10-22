package pl.gleosys.postsdump.application.ports

import arrow.core.Either
import pl.gleosys.postsdump.core.Failure
import pl.gleosys.postsdump.core.Success
import pl.gleosys.postsdump.domain.DumpStatus
import java.util.*

interface NotificationEventPublisher {
    fun notify(id: UUID, status: DumpStatus): Either<Failure, Success>
}