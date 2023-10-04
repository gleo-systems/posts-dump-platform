package pl.gleosys.postsdump.infrastructure.messagebroker

import arrow.core.Either.Companion.catch
import arrow.core.Option
import com.rabbitmq.client.AMQP
import com.rabbitmq.client.Envelope
import com.squareup.moshi.Moshi
import io.github.oshai.kotlinlogging.KotlinLogging
import pl.gleosys.postsdump.application.process.RunDumpProcess
import pl.gleosys.postsdump.domain.toThrowable
import pl.gleosys.postsdump.infrastructure.InfrastructureError
import pl.gleosys.postsdump.infrastructure.InfrastructureException

private val logger = KotlinLogging.logger {}

class PDRequestConsumer(private val parser: Moshi) : MessageConsumer() {
    @Throws(InfrastructureException::class)
    override fun handleDelivery(
        consumerTag: String,
        envelope: Envelope,
        properties: AMQP.BasicProperties,
        body: ByteArray,
    ) {
        logger.debug { "Handling delivery with $properties and body:\n'${String(body)}'" }

        catch {
            Option.fromNullable(parser.adapter(PDRequestDTO::class.java).fromJson(String(body)))
                .toEither { InfrastructureError("Empty delivery body error") }
                .onLeft { throw InfrastructureException(it.toThrowable()) }
                .map(PDRequestDTO::toDomain)
                .onRight { logger.debug { it } }
                .onRight { RunDumpProcess(it).run() }
                .onRight { logger.info { "Successfully handled event with id=${it.id}" } }
        }
            .onLeft { throw InfrastructureException(it) }
    }
}
