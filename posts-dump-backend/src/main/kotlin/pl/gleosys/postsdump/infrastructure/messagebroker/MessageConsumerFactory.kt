package pl.gleosys.postsdump.infrastructure.messagebroker

import com.rabbitmq.client.*

class MessageConsumerFactory(private val delegate: MessageConsumer) {
    fun newInstance(channel: Channel, autoAck: Boolean): Consumer {
        return object : DefaultConsumer(channel) {
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
    }
}
