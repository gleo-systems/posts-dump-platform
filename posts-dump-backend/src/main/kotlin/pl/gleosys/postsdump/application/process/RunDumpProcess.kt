package pl.gleosys.postsdump.application.process

import pl.gleosys.postsdump.domain.PostDumpEvent

class RunDumpProcess(private val event: PostDumpEvent) : PostsDumpProcessCommand {
    override fun run() {
        TODO("To be implemented")
    }
}
