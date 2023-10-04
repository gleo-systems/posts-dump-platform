package pl.gleosys.postsdump.application.ports

import arrow.core.Option
import pl.gleosys.postsdump.domain.Event
import pl.gleosys.postsdump.domain.Failure

interface ProcessCommand {
    fun run(event: Event): Option<Failure>
}
