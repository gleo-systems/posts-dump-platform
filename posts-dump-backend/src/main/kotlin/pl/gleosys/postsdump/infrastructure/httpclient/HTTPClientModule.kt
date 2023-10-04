package pl.gleosys.postsdump.infrastructure.httpclient

import com.google.inject.AbstractModule
import com.google.inject.Provides
import com.squareup.moshi.FromJson
import com.squareup.moshi.Moshi
import com.squareup.moshi.ToJson
import com.squareup.moshi.adapters.Rfc3339DateJsonAdapter
import jakarta.inject.Named
import jakarta.inject.Singleton
import pl.gleosys.postsdump.application.ports.PostsAPIClient
import java.util.*

class HTTPClientModule : AbstractModule() {
    @Provides
    @Singleton
    @Named("moshiParser")
    fun moshiParser(): Moshi = Moshi.Builder()
        .add(Date::class.java, Rfc3339DateJsonAdapter())
        .add(UUIDJsonAdapter())
        .build()

    @Provides
    @Singleton
    fun postsAPIClient(mapper: Moshi): PostsAPIClient = JSONPlaceholderHTTPClient(mapper)
}

class UUIDJsonAdapter {
    @FromJson
    fun fromJson(value: String) = UUID.fromString(value)

    @ToJson
    fun toJson(value: UUID) = value.toString()
}
