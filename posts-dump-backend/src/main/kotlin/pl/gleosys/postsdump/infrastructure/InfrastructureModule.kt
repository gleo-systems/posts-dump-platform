package pl.gleosys.postsdump.infrastructure

import com.google.inject.AbstractModule
import com.google.inject.Scopes
import pl.gleosys.postsdump.infrastructure.httpclient.HTTPClientModule
import pl.gleosys.postsdump.infrastructure.messagebroker.MessageBrokerModule
import pl.gleosys.postsdump.infrastructure.storage.StorageModule

class InfrastructureModule : AbstractModule() {
    override fun configure() {
        install(HTTPClientModule())
        install(StorageModule())
        install(MessageBrokerModule())

        bind(InfrastructureInitializer::class.java).`in`(Scopes.SINGLETON)
    }
}
