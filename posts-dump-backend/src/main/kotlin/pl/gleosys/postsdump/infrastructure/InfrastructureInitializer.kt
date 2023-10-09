package pl.gleosys.postsdump.infrastructure

import io.github.oshai.kotlinlogging.KotlinLogging
import pl.gleosys.postsdump.infrastructure.messagebroker.MessageBrokerSubscriber

private val logger = KotlinLogging.logger {}

class InfrastructureInitializer(private val messageBrokerSubscriber: MessageBrokerSubscriber) {
    fun run() {
        logger.info { "Initializing infrastructure components..." }
        messageBrokerSubscriber.run()
        logger.info { "Successfully initialized infrastructure components" }
    }
}
