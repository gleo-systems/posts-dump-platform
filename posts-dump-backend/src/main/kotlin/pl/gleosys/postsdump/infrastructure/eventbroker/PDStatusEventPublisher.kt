package pl.gleosys.postsdump.infrastructure.eventbroker

import arrow.core.Either
import arrow.core.flatMap
import io.github.oshai.kotlinlogging.KotlinLogging
import pl.gleosys.postsdump.application.ports.NotificationEventPublisher
import pl.gleosys.postsdump.core.Failure
import pl.gleosys.postsdump.core.Success
import pl.gleosys.postsdump.domain.DumpStatus
import java.util.*

private val logger = KotlinLogging.logger {}

class PDStatusEventPublisher(private val service: NotificationEventService) :
    NotificationEventPublisher {
    override fun notify(id: UUID, status: DumpStatus): Either<Failure, Success> =
        PDStatusEventDTO.of(id, status)
            .onRight { event -> logger.debug { "Publishing new $event" } }
            .map { it to PDStatusEventDTO::class.java }
            .flatMap { (event, clazz) ->
                service.send(event, clazz).onRight {
                    logger.info { "Successfully published $event" }
                }
            }
}