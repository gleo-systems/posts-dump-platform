package pl.gleosys.postsdump.infrastructure.rabbitmq

import com.rabbitmq.client.*
import io.github.oshai.kotlinlogging.KotlinLogging

private val logger = KotlinLogging.logger {}

class RabbitMQSubscriber(
    private val properties: ConnectionProperties,
    private val delegate: MessageConsumer,
) {
    fun run() {
        logger.debug { "Creating subscription with $properties" }
        val (username, password, hostName, consumerTag, queueName, autoAck) = properties

        val factory = ConnectionFactory()
        factory.username = username
        factory.password = password
        factory.host = hostName
        val channel = factory.newConnection().createChannel()

        // An alternative is ConsumerFactory creation and injection
        val consumer: Consumer = object : DefaultConsumer(channel) {
            override fun handleDelivery(
                consumerTag: String,
                envelope: Envelope,
                properties: AMQP.BasicProperties,
                body: ByteArray,
            ) {
                delegate.handleDelivery(consumerTag, envelope, properties, body)
                if (!autoAck) this.channel.basicAck(envelope.deliveryTag, false)
            }
        }

        // TODO: add shutdown hook to manage SIGINT and close connection/channel objects

        channel.basicConsume(queueName, autoAck, consumerTag, consumer)
        logger.info { "Successfully created subscription for queueName=$queueName and hostName=$hostName" }
    }
}
