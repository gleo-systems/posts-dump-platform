package pl.gleosys.postsdump.infrastructure.messagebroker

import arrow.core.Either
import arrow.core.Either.Companion.catch
import arrow.core.flatMap
import arrow.core.right
import com.rabbitmq.client.Channel
import com.rabbitmq.client.ConnectionFactory
import io.github.oshai.kotlinlogging.KotlinLogging
import pl.gleosys.postsdump.core.Failure
import pl.gleosys.postsdump.core.Failure.FailureFactory.newInstance
import pl.gleosys.postsdump.core.Failure.InfrastructureError

private val logger = KotlinLogging.logger {}

class MessageBrokerSubscriber(
    private val properties: MessageBrokerProperties,
    private val consumerFactory: MessageConsumerFactory
) {

    // TODO: add shutdown hook to manage SIGINT and close connection/channel objects
    fun run() {
        properties
            .right()
            .onRight { logger.info { "Creating subscription with $properties" } }
            .flatMap(::createChannel)
            .flatMap(::consumeMessages)
            .onRight { logger.info { "Successfully created subscription for channelName=${it.channelName} and hostName=${it.hostName}" } }
    }

    private fun createChannel(properties: MessageBrokerProperties): Either<InfrastructureError, Pair<MessageBrokerProperties, Channel>> {
        val (username, password, hostName) = properties
        return catch {
            ConnectionFactory().apply {
                this.username = username
                this.username = username
                this.password = password
                this.host = hostName
            }
                .newConnection().createChannel()
        }
            .mapLeft<InfrastructureError>(::newInstance)
            .map { properties to it }
    }

    private fun consumeMessages(data: Pair<MessageBrokerProperties, Channel>): Either<Failure, MessageBrokerProperties> {
        val (properties, channel) = data
        return catch {
            channel.basicConsume(
                properties.channelName,
                properties.channelAutoACK,
                properties.consumerTag,
                consumerFactory.newInstance(channel, properties.channelAutoACK)
            )
        }
            .mapLeft<InfrastructureError>(::newInstance)
            .map { properties }
    }
}
