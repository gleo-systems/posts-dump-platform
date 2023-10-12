package pl.gleosys.postsdump.infrastructure.messagebroker

import com.google.inject.AbstractModule
import com.google.inject.Provides
import jakarta.inject.Named
import jakarta.inject.Singleton
import pl.gleosys.postsdump.application.process.RunDumpProcessCommand
import pl.gleosys.postsdump.core.Failure
import pl.gleosys.postsdump.core.InitializationError
import pl.gleosys.postsdump.infrastructure.EnvironmentProperty
import pl.gleosys.postsdump.infrastructure.JSONParser
import pl.gleosys.postsdump.infrastructure.messagebroker.MessageBrokerProperty.CHANNEL_AUTOACK_PROP
import pl.gleosys.postsdump.infrastructure.messagebroker.MessageBrokerProperty.CHANNEL_NAME_PROP
import pl.gleosys.postsdump.infrastructure.messagebroker.MessageBrokerProperty.CONSUMER_TAG_PROP
import pl.gleosys.postsdump.infrastructure.messagebroker.MessageBrokerProperty.HOSTNAME_PROP
import pl.gleosys.postsdump.infrastructure.messagebroker.MessageBrokerProperty.PASSWORD_PROP
import pl.gleosys.postsdump.infrastructure.messagebroker.MessageBrokerProperty.USERNAME_PROP
import java.lang.System.getenv

private enum class MessageBrokerProperty(override val envName: String) : EnvironmentProperty {
    USERNAME_PROP("MESSAGE_BROKER_USERNAME"),
    PASSWORD_PROP("MESSAGE_BROKER_PASSWORD"),
    HOSTNAME_PROP("MESSAGE_BROKER_HOSTNAME"),
    CONSUMER_TAG_PROP("MESSAGE_BROKER_CONSUMER_TAG"),
    CHANNEL_NAME_PROP("MESSAGE_BROKER_CHANNEL_NAME"),
    CHANNEL_AUTOACK_PROP("MESSAGE_BROKER_CHANNEL_AUTOACK")
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
            getenv(CHANNEL_AUTOACK_PROP.envName).toBooleanStrict()
        )
            .mapLeft(Failure::toThrowable)
            .onLeft { throw InitializationError(it) }
            .getOrNull()!!

    @Provides
    @Singleton
    @Named("pdRequestConsumerFactory")
    fun pdRequestConsumerFactory(
        parser: JSONParser,
        command: RunDumpProcessCommand
    ): MessageConsumerFactory = MessageConsumerFactory(parser, command)

    @Provides
    @Singleton
    @Named("pdRequestBrokerSubscriber")
    fun pdRequestBrokerSubscriber(
        @Named("pdRequestBrokerProperties") properties: MessageBrokerProperties,
        @Named("pdRequestConsumerFactory") consumerFactory: MessageConsumerFactory
    ): MessageBrokerSubscriber = MessageBrokerSubscriber(properties, consumerFactory)
}
