package pl.gleosys.postsdump.infrastructure.messagebroker

import arrow.core.Either
import arrow.core.Either.Companion.catch
import arrow.core.Option
import com.rabbitmq.client.AMQP
import com.rabbitmq.client.Envelope
import com.squareup.moshi.Moshi
import io.github.oshai.kotlinlogging.KotlinLogging
import pl.gleosys.postsdump.application.process.RunDumpProcess
import pl.gleosys.postsdump.application.process.RunDumpProcessCommand
import pl.gleosys.postsdump.domain.Failure
import pl.gleosys.postsdump.domain.toThrowable
import pl.gleosys.postsdump.infrastructure.InfrastructureError
import pl.gleosys.postsdump.infrastructure.removeWhitespaceChars

private val logger = KotlinLogging.logger {}

class PDRequestConsumer(private val parser: Moshi, private val process: RunDumpProcess) : MessageConsumer() {
    override fun handleDelivery(
        consumerTag: String,
        envelope: Envelope,
        properties: AMQP.BasicProperties,
        body: ByteArray
    ) {
        logger.debug { "Handling delivery with $properties and body=${String(body).removeWhitespaceChars()}" }

        catch {
            Option.fromNullable(parser.adapter(PDRequestDTO::class.java).fromJson(String(body)))
                .toEither { InfrastructureError(message = "Empty delivery body error") }
                .map(PDRequestDTO::toDomain)
                .onRight { logger.debug { it } }
                .map { RunDumpProcessCommand(process).run(it) }
                .onRight(::logSuccess)
        }
            .mapLeft(::InfrastructureError)
            .fold({ logError(it, body) }, { logRightErrors(it, body) })
    }

    private fun logSuccess(errOpt: Option<Failure>) =
        errOpt.onNone { logger.debug { "Successfully processed posts dump request" } }

    private fun logError(err: Failure, body: ByteArray) =
        logger.error(err.toThrowable()) { "Delivery body=${String(body).removeWhitespaceChars()}" }

    private fun logRightErrors(result: Either<InfrastructureError, Option<Failure>>, body: ByteArray) =
        result.onLeft { logError(it, body) }
            .onRight { errOpt -> errOpt.onSome { logError(it, body) } }
}
