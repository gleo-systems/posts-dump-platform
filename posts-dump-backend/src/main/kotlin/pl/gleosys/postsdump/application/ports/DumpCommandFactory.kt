package pl.gleosys.postsdump.application.ports

import arrow.core.Either
import pl.gleosys.postsdump.core.Failure
import pl.gleosys.postsdump.domain.DumpEvent

interface DumpCommandFactory {
    fun newRunDumpCommand(event: DumpEvent): Either<Failure, DumpCommand>
}
