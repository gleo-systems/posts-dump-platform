package pl.gleosys.postsdump.application.process

import arrow.core.Either
import pl.gleosys.postsdump.application.ports.DumpCommand
import pl.gleosys.postsdump.core.Failure
import pl.gleosys.postsdump.core.Success
import pl.gleosys.postsdump.domain.DumpEvent

class RunDumpProcessCommand(
    private val process: RunDumpProcess,
    private val event: DumpEvent
) :
    DumpCommand {
    override fun run(): Either<Failure, Success> = process.execute(event)
}
