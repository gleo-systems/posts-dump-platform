package pl.gleosys.postsdump.infrastructure.storage

import com.google.inject.AbstractModule
import com.google.inject.Provides
import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.inject.Named
import jakarta.inject.Singleton
import pl.gleosys.postsdump.application.ports.StorageUploader
import pl.gleosys.postsdump.infrastructure.storage.StorageProperty.*
import software.amazon.awssdk.http.urlconnection.UrlConnectionHttpClient
import software.amazon.awssdk.services.s3.S3Client
import java.net.URI

private val logger = KotlinLogging.logger {}

private enum class StorageProperty(val envName: String) {
    STORAGE_API_URI_PROP("MINIO_API_URI"),
    STORAGE_BASE_DIR_PROP("MINIO_BUCKET_NAME"),
}

/**
 * Provides MinIO service integration, can be easily switched to AWS S3 service.
 *
 * Utilizes autoconfiguration feature of AWS SDK library.
 *
 * @see <a href="https://docs.aws.amazon.com/sdk-for-java/latest/developer-guide/using.html">Use the AWS SDK for Java 2.x</a>
 * @see software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider
 * @see software.amazon.awssdk.regions.providers.SystemSettingsRegionProvider
 */
class StorageModule : AbstractModule() {

    @Provides
    @Singleton
    @Named("bucketsStorageUploader")
    fun bucketsStorageUploader(@Named("bucketsStorageClient") client: StorageClient): StorageUploader {
        return BucketsStorageUploader(client)
    }

    @Provides
    @Singleton
    @Named("bucketsStorageClient")
    fun bucketsStorageClient(
        @Named("storageProperties") properties: StorageProperties,
        @Named("minioServiceClient") delegate: S3Client,
    ): StorageClient {
        return BucketsStorageClient(properties, delegate)
    }

    @Provides
    @Singleton
    @Named("minioServiceClient")
    fun minioServiceClient(@Named("storageProperties") properties: StorageProperties): S3Client {
        logger.debug { "Creating buckets storage HTTP client with $properties" }
        return S3Client.builder()
            .httpClient(UrlConnectionHttpClient.builder().build())
            .endpointOverride(URI.create(properties.apiURI))
            .forcePathStyle(true)
            .build()
    }

    @Provides
    @Singleton
    @Named("storageProperties")
    fun storageProperties(): StorageProperties {
        return StorageProperties(
            System.getenv(STORAGE_API_URI_PROP.envName),
            System.getenv(STORAGE_BASE_DIR_PROP.envName),
        )
    }
}
