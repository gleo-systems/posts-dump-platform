package pl.gleosys.postsdump.infrastructure.rabbitmq

import com.google.inject.AbstractModule
import com.google.inject.Provides
import com.google.inject.Scopes
import com.google.inject.name.Names
import jakarta.inject.Named
import jakarta.inject.Singleton
import pl.gleosys.postsdump.infrastructure.rabbitmq.RabbitMQProperty.*
import java.lang.System.getenv

private enum class RabbitMQProperty(val envName: String) {
    USERNAME_PROP("RABBITMQ_USERNAME"),
    PASSWORD_PROP("RABBITMQ_PASSWORD"),
    HOSTNAME_PROP("RABBITMQ_HOSTNAME"),
    CONSUMER_TAG_PROP("RABBITMQ_CONSUMER_TAG"),
    PDR_QUEUE_NAME_PROP("PDR_QUEUE_NAME"),
    PDR_QUEUE_AUTOACK_PROP("PDR_QUEUE_AUTOACK"),
}

// TODO: provide request processed notification queue configuration
class RabbitMQModule : AbstractModule() {
    override fun configure() {
        bind(MessageConsumer::class.java)
            .annotatedWith(Names.named("pdRequestConsumer"))
            .to(PDRequestConsumer::class.java)
            .`in`(Scopes.SINGLETON)
    }

    @Provides
    @Singleton
    @Named("pdRequestsQueueSubscriber")
    fun pdRequestsQueueSubscriber(
        @Named("pdRequestConsumer") consumer: MessageConsumer,
        @Named("pdRequestsQueueProperties") properties: ConnectionProperties,
    ): RabbitMQSubscriber {
        return RabbitMQSubscriber(properties, consumer)
    }

    @Provides
    @Singleton
    @Named("pdRequestsQueueProperties")
    fun pdRequestsQueueProperties(): ConnectionProperties {
        return ConnectionProperties(
            getenv(USERNAME_PROP.envName),
            getenv(PASSWORD_PROP.envName),
            getenv(HOSTNAME_PROP.envName),
            getenv(CONSUMER_TAG_PROP.envName),
            getenv(PDR_QUEUE_NAME_PROP.envName),
            getenv(PDR_QUEUE_AUTOACK_PROP.envName).toBooleanStrict(),
        )
    }
}
