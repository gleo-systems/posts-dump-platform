package pl.gleosys.postsdump

import com.google.inject.AbstractModule
import pl.gleosys.postsdump.application.ApplicationModule
import pl.gleosys.postsdump.domain.DomainModule
import pl.gleosys.postsdump.infrastructure.InfrastructureModule

class MainModule : AbstractModule() {
    override fun configure() {
        install(DomainModule())
        install(ApplicationModule())
        install(InfrastructureModule())
    }
}
