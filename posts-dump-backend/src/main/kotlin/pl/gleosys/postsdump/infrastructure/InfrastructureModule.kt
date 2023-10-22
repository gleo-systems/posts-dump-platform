package pl.gleosys.postsdump.infrastructure

import com.google.inject.AbstractModule
import com.google.inject.Provides
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapters.Rfc3339DateJsonAdapter
import jakarta.inject.Named
import jakarta.inject.Singleton
import pl.gleosys.postsdump.infrastructure.eventbroker.EventBrokerModule
import pl.gleosys.postsdump.infrastructure.httpclient.HTTPClientModule
import pl.gleosys.postsdump.infrastructure.httpclient.UUIDJsonAdapter
import pl.gleosys.postsdump.infrastructure.messagebroker.MessageBrokerModule
import pl.gleosys.postsdump.infrastructure.messagebroker.MessageBrokerSubscriber
import pl.gleosys.postsdump.infrastructure.storage.StorageModule
import java.util.*

class InfrastructureModule : AbstractModule() {
    override fun configure() {
        install(HTTPClientModule())
        install(StorageModule())
        install(MessageBrokerModule())
        install(EventBrokerModule())
    }

    @Provides
    @Singleton
    fun jsonParser(): JSONParser =
        JSONParser(
            Moshi.Builder()
                .add(Date::class.java, Rfc3339DateJsonAdapter())
                .add(UUIDJsonAdapter())
                .build()
        )

    @Provides
    @Singleton
    fun infrastructureInitializer(
        @Named("pdRequestBrokerSubscriber") messageBrokerSubscriber: MessageBrokerSubscriber
    ): InfrastructureInitializer = InfrastructureInitializer(messageBrokerSubscriber)
}
