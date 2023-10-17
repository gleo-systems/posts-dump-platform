package pl.gleosys.postsdump.application.process

import arrow.core.Either
import pl.gleosys.postsdump.application.ports.ProcessCommand
import pl.gleosys.postsdump.domain.Event
import pl.gleosys.postsdump.util.Failure
import pl.gleosys.postsdump.util.Success

class RunDumpProcessCommand(private val process: RunDumpProcess) : ProcessCommand {
    override fun run(event: Event): Either<Failure, Success> = process.execute(event)
}
