package pl.gleosys.postsdump.application.process

import arrow.core.Either
import arrow.core.Option
import arrow.core.flatMap
import arrow.core.right
import pl.gleosys.postsdump.application.ports.DumpCommand
import pl.gleosys.postsdump.application.ports.DumpCommandFactory
import pl.gleosys.postsdump.application.ports.PostsAPIClient
import pl.gleosys.postsdump.application.ports.StorageUploader
import pl.gleosys.postsdump.core.Failure
import pl.gleosys.postsdump.core.Failure.ApplicationError
import pl.gleosys.postsdump.domain.DumpEvent
import pl.gleosys.postsdump.domain.StorageType

class DefaultDumpCommandFactory(
    private val apiClient: PostsAPIClient,
    private val uploaderMap: Map<StorageType, StorageUploader>
) : DumpCommandFactory {

    override fun newRunDumpCommand(event: DumpEvent): Either<Failure, DumpCommand> {
        return event
            .right()
            .flatMap(::findUploader)
            .map { (storageUploader, event) ->
                Pair(RunDumpProcess(apiClient, storageUploader), event)
            }
            .map { (process, event) -> RunDumpProcessCommand(process, event) }
    }

    private fun findUploader(event: DumpEvent): Either<Failure, Pair<StorageUploader, DumpEvent>> {
        return event.right()
            .flatMap { e ->
                Option.fromNullable(uploaderMap[e.storageType])
                    .toEither { ApplicationError("Could not find valid storage uploader for type=${e.storageType}") }
                    .map { Pair(it, event) }
            }
    }
}
