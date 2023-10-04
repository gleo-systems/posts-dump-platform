package pl.gleosys.postsdump.domain

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Post(val userId: Long, val id: Long, val title: String, val body: String)
