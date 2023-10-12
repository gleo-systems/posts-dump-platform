package pl.gleosys.postsdump.infrastructure.httpclient

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import java.util.*

class UUIDJsonAdapter {
    @FromJson
    fun fromJson(value: String) = UUID.fromString(value)

    @ToJson
    fun toJson(value: UUID) = value.toString()
}
