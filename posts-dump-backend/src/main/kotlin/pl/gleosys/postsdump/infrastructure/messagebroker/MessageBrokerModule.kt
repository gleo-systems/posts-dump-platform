package pl.gleosys.postsdump.infrastructure.messagebroker

import com.google.inject.AbstractModule
import com.google.inject.Provides
import com.squareup.moshi.Moshi
import jakarta.inject.Named
import jakarta.inject.Singleton
import pl.gleosys.postsdump.infrastructure.EnvironmentProperty
import pl.gleosys.postsdump.infrastructure.messagebroker.MessageBrokerProperty.*
import java.lang.System.getenv

private enum class MessageBrokerProperty(override val envName: String) : EnvironmentProperty {
    USERNAME_PROP("MESSAGE_BROKER_USERNAME"),
    PASSWORD_PROP("MESSAGE_BROKER_PASSWORD"),
    HOSTNAME_PROP("MESSAGE_BROKER_HOSTNAME"),
    CONSUMER_TAG_PROP("MESSAGE_BROKER_CONSUMER_TAG"),
    CHANNEL_NAME_PROP("MESSAGE_BROKER_CHANNEL_NAME"),
    CHANNEL_AUTOACK_PROP("MESSAGE_BROKER_CHANNEL_AUTOACK"),
}

class MessageBrokerModule : AbstractModule() {

    @Provides
    @Singleton
    @Named("pdRequestBrokerProperties")
    fun pdRequestBrokerProperties(): MessageBrokerProperties =
        MessageBrokerProperties(
            getenv(USERNAME_PROP.envName),
            getenv(PASSWORD_PROP.envName),
            getenv(HOSTNAME_PROP.envName),
            getenv(CONSUMER_TAG_PROP.envName),
            getenv(CHANNEL_NAME_PROP.envName),
            getenv(CHANNEL_AUTOACK_PROP.envName).toBooleanStrict(),
        )

    @Provides
    @Singleton
    @Named("pdRequestConsumer")
    fun pdRequestConsumer(parser: Moshi): MessageConsumer = PDRequestConsumer(parser)

    @Provides
    @Singleton
    fun messageConsumerFactory(@Named("pdRequestConsumer") delegate: MessageConsumer): MessageConsumerFactory =
        MessageConsumerFactory(delegate)

    @Provides
    @Singleton
    @Named("pdRequestBrokerSubscriber")
    fun pdRequestBrokerSubscriber(
        factory: MessageConsumerFactory,
        @Named("pdRequestBrokerProperties") properties: MessageBrokerProperties,
    ): MessageBrokerSubscriber = MessageBrokerSubscriber(properties, factory)
}
