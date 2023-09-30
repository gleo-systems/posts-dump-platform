package pl.gleosys.postsdump

import com.google.inject.Guice
import io.github.oshai.kotlinlogging.KotlinLogging
import pl.gleosys.postsdump.infrastructure.InfrastructureInitializer
import kotlin.system.exitProcess

private val logger = KotlinLogging.logger {}

fun main() {
    logger.info { "Starting application server..." }
    try {
        val context = Guice.createInjector(MainModule())
        context.getInstance(InfrastructureInitializer::class.java).run()
        logger.info { "Successfully started application server" }
    } catch (t: Throwable) {
        logger.error(t) { "Error while starting application server" }
        exitProcess(-1)
    }
}
