package pl.gleosys.postsdump.application

import com.google.inject.AbstractModule
import com.google.inject.Provides
import jakarta.inject.Named
import jakarta.inject.Singleton
import pl.gleosys.postsdump.application.ports.PostsAPIClient
import pl.gleosys.postsdump.application.ports.StorageUploader
import pl.gleosys.postsdump.application.process.RunDumpProcess

class ApplicationModule : AbstractModule() {
    @Provides
    @Singleton
    fun runDumpProcess(
        apiClient: PostsAPIClient,
        @Named("bucketsStorageUploader") storage: StorageUploader,
    ): RunDumpProcess = RunDumpProcess(apiClient, storage)
}
