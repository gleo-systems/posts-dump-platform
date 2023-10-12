package pl.gleosys.postsdump.infrastructure.httpclient

import com.google.inject.AbstractModule
import com.google.inject.Provides
import jakarta.inject.Singleton
import pl.gleosys.postsdump.application.ports.PostsAPIClient
import pl.gleosys.postsdump.infrastructure.JSONParser

class HTTPClientModule : AbstractModule() {
    @Provides
    @Singleton
    fun postsAPIClient(parser: JSONParser): PostsAPIClient = JSONPlaceholderHTTPClient(parser)
}
