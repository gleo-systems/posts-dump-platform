package pl.gleosys.postsdump.infrastructure.messagebroker

import com.rabbitmq.client.AMQP
import com.rabbitmq.client.Consumer
import com.rabbitmq.client.Envelope
import com.rabbitmq.client.ShutdownSignalException
import java.io.IOException

/**
 * Template class providing a link between implementation and consumer interface.
 */
abstract class MessageConsumer : Consumer {
    @Throws(IOException::class)
    abstract override fun handleDelivery(
        consumerTag: String,
        envelope: Envelope,
        properties: AMQP.BasicProperties,
        body: ByteArray,
    )

    final override fun handleConsumeOk(consumerTag: String) {
        throw UnsupportedOperationException("Forbidden call")
    }

    final override fun handleCancelOk(consumerTag: String) {
        throw UnsupportedOperationException("Forbidden call")
    }

    @Throws(IOException::class)
    final override fun handleCancel(consumerTag: String) {
        throw UnsupportedOperationException("Forbidden call")
    }

    final override fun handleShutdownSignal(consumerTag: String, sig: ShutdownSignalException) {
        throw UnsupportedOperationException("Forbidden call")
    }

    final override fun handleRecoverOk(consumerTag: String) {
        throw UnsupportedOperationException("Forbidden call")
    }
}
