package pl.gleosys.postsdump.infrastructure.httpclient

import com.squareup.moshi.JsonClass
import pl.gleosys.postsdump.domain.Post

@JsonClass(generateAdapter = true)
data class PostDTO(val userId: Long, val id: Long, val title: String, val body: String)

fun PostDTO.toDomain() = Post(userId, id, title, body)
