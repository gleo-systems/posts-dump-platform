package pl.gleosys.postsdump.application.process

import pl.gleosys.postsdump.domain.Event
import pl.gleosys.postsdump.domain.Post
import java.nio.file.Path

private enum class FileExtension(val suffix: String) {
    JSON_EXT(".json")
}

class Path {
    companion object {
        fun jsonExtOf(event: Event, post: Post): Path =
            Path.of(event.id.toString(), "${post.id}${FileExtension.JSON_EXT.suffix}")
    }
}
