package pl.gleosys.postsdump.infrastructure

import com.google.inject.AbstractModule
import com.google.inject.Provides
import jakarta.inject.Named
import jakarta.inject.Singleton
import pl.gleosys.postsdump.infrastructure.httpclient.HTTPClientModule
import pl.gleosys.postsdump.infrastructure.messagebroker.MessageBrokerModule
import pl.gleosys.postsdump.infrastructure.messagebroker.MessageBrokerSubscriber
import pl.gleosys.postsdump.infrastructure.storage.StorageModule

class InfrastructureModule : AbstractModule() {
    override fun configure() {
        install(HTTPClientModule())
        install(StorageModule())
        install(MessageBrokerModule())
    }

    @Provides
    @Singleton
    fun infrastructureInitializer(
        @Named("pdRequestBrokerSubscriber") messageBrokerSubscriber: MessageBrokerSubscriber
    ): InfrastructureInitializer = InfrastructureInitializer(messageBrokerSubscriber)
}
