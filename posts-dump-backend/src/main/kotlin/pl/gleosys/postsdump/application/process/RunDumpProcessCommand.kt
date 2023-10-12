package pl.gleosys.postsdump.application.process

import arrow.core.Either
import pl.gleosys.postsdump.application.ports.ProcessCommand
import pl.gleosys.postsdump.core.Failure
import pl.gleosys.postsdump.core.Success
import pl.gleosys.postsdump.domain.Event

class RunDumpProcessCommand(private val process: RunDumpProcess) : ProcessCommand {
    override fun run(event: Event): Either<Failure, Success> = process.execute(event)
}
