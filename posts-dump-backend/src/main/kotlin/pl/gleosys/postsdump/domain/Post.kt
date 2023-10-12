package pl.gleosys.postsdump.domain

import com.squareup.moshi.JsonClass
import pl.gleosys.postsdump.core.removeWhitespaceChars

@JsonClass(generateAdapter = true)
data class Post(val userId: Long, val id: Long, val title: String, val body: String) {
    override fun toString(): String =
        "Post(userId=$userId, id=$id, title=$title, body=${body.removeWhitespaceChars()})"
}
