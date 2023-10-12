package pl.gleosys.postsdump

import arrow.core.Either.Companion.catch
import com.google.inject.Guice
import io.github.oshai.kotlinlogging.KotlinLogging
import pl.gleosys.postsdump.infrastructure.InfrastructureInitializer
import kotlin.system.exitProcess

private val logger = KotlinLogging.logger {}

fun main() {
    logger.info { "Starting application server..." }
    catch {
        Guice.createInjector(MainModule())
            .getInstance(InfrastructureInitializer::class.java).run()
    }
        .onRight { logger.info { "Successfully started application server" } }
        .onLeft {
            logger.error(it) { "Error while starting application server" }
            exitProcess(-1)
        }
}
