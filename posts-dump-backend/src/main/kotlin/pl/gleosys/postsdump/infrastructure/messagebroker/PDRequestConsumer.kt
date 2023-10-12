package pl.gleosys.postsdump.infrastructure.messagebroker

import arrow.core.Either.Companion.catch
import arrow.core.flatMap
import arrow.core.flatten
import com.rabbitmq.client.AMQP
import com.rabbitmq.client.Channel
import com.rabbitmq.client.DefaultConsumer
import com.rabbitmq.client.Envelope
import io.github.oshai.kotlinlogging.KotlinLogging
import pl.gleosys.postsdump.application.process.RunDumpProcessCommand
import pl.gleosys.postsdump.core.Failure
import pl.gleosys.postsdump.core.Failure.FailureFactory
import pl.gleosys.postsdump.core.Failure.InfrastructureError
import pl.gleosys.postsdump.core.removeWhitespaceChars
import pl.gleosys.postsdump.infrastructure.JSONParser

private val logger = KotlinLogging.logger {}

class PDRequestConsumer(
    private val parser: JSONParser,
    private val command: RunDumpProcessCommand,
    private val channel: Channel,
    private val autoACK: Boolean
) : DefaultConsumer(channel) {

    override fun handleDelivery(
        consumerTag: String,
        envelope: Envelope,
        properties: AMQP.BasicProperties,
        body: ByteArray
    ) {
        logger.debug { "Handling delivery with $properties and body '${String(body).removeWhitespaceChars()}'" }

        catch {
            parser.fromJSON(String(body), PDRequestDTO::class.java)
                .map(PDRequestDTO::toDomain)
                .flatMap(command::run)
                .onRight { performACK(envelope.deliveryTag) }
        }
            .mapLeft<InfrastructureError>(FailureFactory::newInstance)
            .flatten()
            .mapLeft(Failure::toThrowable)
            .onLeft {
                logger.error(it) { "Delivery body=${String(body).removeWhitespaceChars()} and $properties" }
            }
            .onRight {
                logger.debug { "Successfully processed posts dump request with body '${String(body).removeWhitespaceChars()}'" }
            }
    }

    private fun performACK(deliveryTag: Long) {
        if (!autoACK) this.channel.basicAck(deliveryTag, false)
    }
}
