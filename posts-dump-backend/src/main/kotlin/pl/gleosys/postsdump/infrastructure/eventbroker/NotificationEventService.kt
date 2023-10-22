package pl.gleosys.postsdump.infrastructure.eventbroker

import arrow.core.Either
import arrow.core.Either.Companion.catch
import arrow.core.flatMap
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerRecord
import pl.gleosys.postsdump.core.Failure
import pl.gleosys.postsdump.core.Failure.InfrastructureError
import pl.gleosys.postsdump.core.Success
import pl.gleosys.postsdump.infrastructure.JSONParser

class NotificationEventService(
    private val properties: EventBrokerProperties,
    private val client: KafkaProducer<String, ByteArray>,
    private val parser: JSONParser
) {
    fun <V : PDEvent> send(event: V, clazz: Class<V>): Either<Failure, Success> {
        return parser.toByteArray(event, clazz)
            .map { properties.notificationsTopicName to it }
            .map { (topicName, bytes) -> ProducerRecord<String, ByteArray>(topicName, bytes) }
            .flatMap(this::sendRecord)
    }

    private fun sendRecord(record: ProducerRecord<String, ByteArray>): Either<InfrastructureError, Success> {
        return catch {
            client.send(record).get()
        }
            .mapLeft<InfrastructureError>(Failure.FailureFactory::newInstance)
            .map { Success }
    }
}
