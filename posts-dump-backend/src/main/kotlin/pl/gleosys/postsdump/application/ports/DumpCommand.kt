package pl.gleosys.postsdump.application.ports

import arrow.core.Either
import pl.gleosys.postsdump.core.Failure
import pl.gleosys.postsdump.core.Success

interface DumpCommand {
    fun run(): Either<Failure, Success>
}
