package pl.gleosys.postsdump.infrastructure.rabbitmq

import com.rabbitmq.client.AMQP
import com.rabbitmq.client.Envelope
import io.github.oshai.kotlinlogging.KotlinLogging

private val logger = KotlinLogging.logger {}

class PDRequestConsumer() : MessageConsumer() {
    override fun handleDelivery(
        consumerTag: String,
        envelope: Envelope,
        properties: AMQP.BasicProperties,
        body: ByteArray,
    ) {
        logger.debug { "Handling message with deliveryTag=${envelope.deliveryTag} and body:\n'${String(body)}'" }

        // TODO: delivery tag count from 0 on each instance!

        logger.info { "Successfully handled message with deliveryTag=${envelope.deliveryTag}" }
    }
}
