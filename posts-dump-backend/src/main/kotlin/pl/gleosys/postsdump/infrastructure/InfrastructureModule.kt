package pl.gleosys.postsdump.infrastructure

import com.google.inject.AbstractModule
import com.google.inject.Scopes
import pl.gleosys.postsdump.infrastructure.rabbitmq.RabbitMQModule
import pl.gleosys.postsdump.infrastructure.storage.StorageModule

class InfrastructureModule : AbstractModule() {
    override fun configure() {
        install(StorageModule())
        install(RabbitMQModule())
        bind(InfrastructureInitializer::class.java).`in`(Scopes.SINGLETON)
    }
}
