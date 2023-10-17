package pl.gleosys.postsdump.application

import com.google.inject.AbstractModule
import com.google.inject.Provides
import com.google.inject.matcher.Matchers
import jakarta.inject.Singleton
import pl.gleosys.postsdump.application.ports.DumpCommandFactory
import pl.gleosys.postsdump.application.ports.PostsAPIClient
import pl.gleosys.postsdump.application.ports.StorageUploader
import pl.gleosys.postsdump.application.process.DefaultDumpCommandFactory
import pl.gleosys.postsdump.core.aop.LogDuration
import pl.gleosys.postsdump.core.aop.LogDurationInterceptor
import pl.gleosys.postsdump.domain.StorageType

class ApplicationModule : AbstractModule() {
    override fun configure() {
        bindInterceptor(
            Matchers.any(),
            Matchers.annotatedWith(LogDuration::class.java),
            LogDurationInterceptor()
        )
    }

    @Provides
    @Singleton
    fun dumpProcessCommandFactory(
        apiClient: PostsAPIClient,
        uploaderMap: Map<StorageType, StorageUploader>
    ): DumpCommandFactory = DefaultDumpCommandFactory(apiClient, uploaderMap)
}
