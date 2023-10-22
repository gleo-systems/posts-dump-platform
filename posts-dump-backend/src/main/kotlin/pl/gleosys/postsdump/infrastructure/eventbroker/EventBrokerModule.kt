package pl.gleosys.postsdump.infrastructure.eventbroker

import com.google.inject.AbstractModule
import com.google.inject.Provides
import jakarta.inject.Singleton
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerConfig.BOOTSTRAP_SERVERS_CONFIG
import org.apache.kafka.clients.producer.ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG
import org.apache.kafka.clients.producer.ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG
import org.apache.kafka.common.serialization.ByteArraySerializer
import org.apache.kafka.common.serialization.StringSerializer
import pl.gleosys.postsdump.application.ports.NotificationEventPublisher
import pl.gleosys.postsdump.core.Failure
import pl.gleosys.postsdump.core.InitializationError
import pl.gleosys.postsdump.infrastructure.EnvironmentProperty
import pl.gleosys.postsdump.infrastructure.JSONParser
import pl.gleosys.postsdump.infrastructure.eventbroker.EventBrokerProperty.API_URL_PROP
import pl.gleosys.postsdump.infrastructure.eventbroker.EventBrokerProperty.NOTIFICATIONS_TOPIC_NAME_PROP

private enum class EventBrokerProperty(override val envName: String) : EnvironmentProperty {
    NOTIFICATIONS_TOPIC_NAME_PROP("EVENT_BROKER_NOTIFICATIONS_TOPIC_NAME"),
    API_URL_PROP("EVENT_BROKER_API_URL")
}

class EventBrokerModule : AbstractModule() {

    @Provides
    @Singleton
    fun eventBrokerProperties(): EventBrokerProperties =
        EventBrokerProperties(
            System.getenv(NOTIFICATIONS_TOPIC_NAME_PROP.envName),
            System.getenv(API_URL_PROP.envName)
        )
            .mapLeft(Failure::toThrowable)
            .onLeft { throw InitializationError(it) }
            .getOrNull()!!

    @Provides
    @Singleton
    fun notificationEventService(
        properties: EventBrokerProperties,
        parser: JSONParser
    ): NotificationEventService {
        val client = KafkaProducer<String, ByteArray>(
            mapOf(
                BOOTSTRAP_SERVERS_CONFIG to properties.apiURL,
                KEY_SERIALIZER_CLASS_CONFIG to StringSerializer::class.qualifiedName,
                VALUE_SERIALIZER_CLASS_CONFIG to ByteArraySerializer::class.qualifiedName
            )
        )
        return NotificationEventService(properties, client, parser)
    }

    @Provides
    @Singleton
    fun notificationEventPublisher(service: NotificationEventService): NotificationEventPublisher =
        PDStatusEventPublisher(service)
}
