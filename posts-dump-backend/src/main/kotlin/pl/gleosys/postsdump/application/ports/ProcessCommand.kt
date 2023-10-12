package pl.gleosys.postsdump.application.ports

import pl.gleosys.postsdump.core.Failure
import pl.gleosys.postsdump.core.Success
import pl.gleosys.postsdump.domain.Event

interface ProcessCommand {
    fun run(event: Event): arrow.core.Either<Failure, Success>
}
