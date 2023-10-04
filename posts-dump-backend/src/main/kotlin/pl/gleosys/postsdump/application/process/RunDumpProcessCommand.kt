package pl.gleosys.postsdump.application.process

import io.github.oshai.kotlinlogging.KotlinLogging
import pl.gleosys.postsdump.application.ports.ProcessCommand
import pl.gleosys.postsdump.domain.Event

private val logger = KotlinLogging.logger {}

class RunDumpProcessCommand(private val process: RunDumpProcess) : ProcessCommand {
    override fun run(event: Event) = process.execute(event)
}
