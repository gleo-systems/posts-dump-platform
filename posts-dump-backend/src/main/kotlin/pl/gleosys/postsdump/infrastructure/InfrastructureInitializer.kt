package pl.gleosys.postsdump.infrastructure

import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.inject.Inject
import jakarta.inject.Named
import pl.gleosys.postsdump.infrastructure.messagebroker.MessageBrokerSubscriber

private val logger = KotlinLogging.logger {}

class InfrastructureInitializer @Inject constructor(
    @Named("pdRequestBrokerSubscriber") private val messageBrokerSubscriber: MessageBrokerSubscriber
) {
    fun run() {
        logger.info { "Initializing infrastructure components..." }
        messageBrokerSubscriber.run()
        logger.info { "Successfully initialized infrastructure components" }
    }
}
