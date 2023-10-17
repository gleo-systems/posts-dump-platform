package pl.gleosys.postsdump.infrastructure.messagebroker

import com.rabbitmq.client.Channel
import com.rabbitmq.client.Consumer
import pl.gleosys.postsdump.application.ports.DumpCommandFactory
import pl.gleosys.postsdump.infrastructure.JSONParser

class MessageConsumerFactory(
    private val parser: JSONParser,
    private val commandFactory: DumpCommandFactory
) {
    fun newInstance(channel: Channel, autoAck: Boolean): Consumer =
        PDRequestConsumer(parser, commandFactory, channel, autoAck)
}
