package pl.gleosys.postsdump.application.ports

import pl.gleosys.postsdump.domain.Event
import pl.gleosys.postsdump.util.Failure
import pl.gleosys.postsdump.util.Success

interface ProcessCommand {
    fun run(event: Event): arrow.core.Either<Failure, Success>
}
