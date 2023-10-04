package pl.gleosys.postsdump.infrastructure.messagebroker

import com.rabbitmq.client.ConnectionFactory
import io.github.oshai.kotlinlogging.KotlinLogging

private val logger = KotlinLogging.logger {}

class MessageBrokerSubscriber(
    private val properties: MessageBrokerProperties,
    private val consumerFactory: MessageConsumerFactory,
) {
    fun run() {
        logger.debug { "Creating subscription with $properties" }
        val (username, password, hostName, consumerTag, channelName, autoAck) = properties

        // TODO: handle exceptions
        val channel = ConnectionFactory().apply {
            this.username = username
            this.username = username
            this.password = password
            this.host = hostName
        }
            .newConnection().createChannel()

        // TODO: add shutdown hook to manage SIGINT and close connection/channel objects

        channel.basicConsume(
            channelName,
            autoAck,
            consumerTag,
            consumerFactory.newInstance(channel, autoAck),
        )
        logger.info { "Successfully created subscription for channelName=$channelName and hostName=$hostName" }
    }
}
