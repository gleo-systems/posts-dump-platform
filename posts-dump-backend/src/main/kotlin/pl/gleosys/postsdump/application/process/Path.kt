package pl.gleosys.postsdump.application.process

import pl.gleosys.postsdump.domain.DumpEvent
import pl.gleosys.postsdump.domain.Post
import java.nio.file.Path

class Path {
    companion object {
        private const val JSON_EXT = ".json"

        @JvmStatic
        fun jsonExtOf(event: DumpEvent, post: Post): Path =
            Path.of(event.id.toString(), "${post.id}$JSON_EXT")
    }
}
