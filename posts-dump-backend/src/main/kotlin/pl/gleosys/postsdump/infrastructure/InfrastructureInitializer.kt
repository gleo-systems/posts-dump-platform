package pl.gleosys.postsdump.infrastructure

import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.inject.Inject
import jakarta.inject.Named
import pl.gleosys.postsdump.infrastructure.rabbitmq.RabbitMQSubscriber

private val logger = KotlinLogging.logger {}

class InfrastructureInitializer @Inject constructor(
    @Named("pdRequestsQueueSubscriber") private val pdRequestsQueueSubscriber: RabbitMQSubscriber,
) {
    fun run() {
        logger.info { "Initializing infrastructure components..." }
        pdRequestsQueueSubscriber.run()
        logger.info { "Successfully initialized infrastructure components" }
    }
}
