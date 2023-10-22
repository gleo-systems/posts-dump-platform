package pl.gleosys.postsdump.infrastructure.messagebroker

import arrow.core.Either
import arrow.core.Either.Companion.catch
import arrow.core.flatMap
import arrow.core.right
import com.rabbitmq.client.AMQP
import com.rabbitmq.client.Channel
import com.rabbitmq.client.DefaultConsumer
import com.rabbitmq.client.Envelope
import io.github.oshai.kotlinlogging.KotlinLogging
import pl.gleosys.postsdump.application.ports.DumpCommand
import pl.gleosys.postsdump.application.ports.DumpCommandFactory
import pl.gleosys.postsdump.core.Failure
import pl.gleosys.postsdump.core.Failure.FailureFactory.newInstance
import pl.gleosys.postsdump.core.Failure.InfrastructureError
import pl.gleosys.postsdump.core.removeWhitespaceChars
import pl.gleosys.postsdump.domain.DumpEvent
import pl.gleosys.postsdump.infrastructure.JSONParser

private val logger = KotlinLogging.logger {}

class PDRequestConsumer(
    private val parser: JSONParser,
    private val commandFactory: DumpCommandFactory,
    private val channel: Channel,
    private val autoACK: Boolean
) : DefaultConsumer(channel) {

    // TODO: add contextId to logger
    override fun handleDelivery(
        consumerTag: String,
        envelope: Envelope,
        properties: AMQP.BasicProperties,
        body: ByteArray
    ) {
        Delivery(consumerTag, envelope, properties, body)
            .right()
            .onRight { logger.info { "Handling posts dump request for $it" } }
            .flatMap(::mapToDomain)
            .flatMap { (delivery, event) ->
                commandFactory.newRunDumpCommand(event)
                    .flatMap(DumpCommand::run)
                    .map { delivery }
            }
            .flatMap(::performACK)
            .onLeft { logger.error(it.toThrowable()) { "Error while handling posts dump request" } }
            .onRight {
                logger.info { "Successfully handled posts dump request with body '${String(it.body).removeWhitespaceChars()}'" }
            }
    }

    private fun mapToDomain(delivery: Delivery): Either<Failure, Pair<Delivery, DumpEvent>> {
        return parser.fromJSON(String(delivery.body), PDRequestDTO::class.java)
            .flatMap(PDRequestDTO::toDomain)
            .onRight { event -> logger.trace { "Mapped to $event" } }
            .map { event -> delivery to event }
    }

    private fun performACK(delivery: Delivery): Either<Failure, Delivery> {
        return catch {
            if (!autoACK) this.channel.basicAck(delivery.envelope.deliveryTag, false)
        }
            .mapLeft<InfrastructureError>(::newInstance)
            .map { delivery }
    }
}

private class Delivery(
    val consumerTag: String,
    val envelope: Envelope,
    val properties: AMQP.BasicProperties,
    val body: ByteArray
) {
    override fun toString(): String =
        "Delivery(" +
            "consumerTag=$consumerTag, " +
            "envelope=$envelope, " +
            "properties=$properties, " +
            "body=${body.contentToString().removeWhitespaceChars()})"
}
