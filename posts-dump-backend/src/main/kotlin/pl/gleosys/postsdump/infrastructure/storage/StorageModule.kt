package pl.gleosys.postsdump.infrastructure.storage

import com.google.inject.AbstractModule
import com.google.inject.Provides
import com.google.inject.multibindings.MapKey
import com.google.inject.multibindings.ProvidesIntoMap
import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.inject.Named
import jakarta.inject.Singleton
import pl.gleosys.postsdump.application.ports.StorageUploader
import pl.gleosys.postsdump.core.Failure
import pl.gleosys.postsdump.core.InitializationError
import pl.gleosys.postsdump.domain.StorageType
import pl.gleosys.postsdump.infrastructure.EnvironmentProperty
import pl.gleosys.postsdump.infrastructure.JSONParser
import pl.gleosys.postsdump.infrastructure.storage.FileSystemProperty.BASE_DIRECTORY_PROP
import pl.gleosys.postsdump.infrastructure.storage.StorageProperty.ACCESS_KEY_ID_PROP
import pl.gleosys.postsdump.infrastructure.storage.StorageProperty.API_URL_PROP
import pl.gleosys.postsdump.infrastructure.storage.StorageProperty.BASE_LOCATION_PROP
import pl.gleosys.postsdump.infrastructure.storage.StorageProperty.REGION_PROP
import pl.gleosys.postsdump.infrastructure.storage.StorageProperty.SECRET_ACCESS_KEY_PROP
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.http.urlconnection.UrlConnectionHttpClient
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.s3.S3Client
import java.net.URI

private val logger = KotlinLogging.logger {}

private enum class StorageProperty(override val envName: String) : EnvironmentProperty {
    REGION_PROP("STORAGE_REGION"),
    ACCESS_KEY_ID_PROP("STORAGE_ACCESS_KEY_ID"),
    SECRET_ACCESS_KEY_PROP("STORAGE_SECRET_ACCESS_KEY"),
    API_URL_PROP("STORAGE_API_URL"),
    BASE_LOCATION_PROP("STORAGE_BASE_LOCATION")
}

private enum class FileSystemProperty(override val envName: String) : EnvironmentProperty {
    BASE_DIRECTORY_PROP("FILE_SYSTEM_BASE_DIRECTORY")
}

/**
 * Provides MinIO service integration, can be easily switched to AWS S3 service.
 *
 * Utilizes autoconfiguration feature of AWS SDK library.
 *
 * @see <a href="https://docs.aws.amazon.com/sdk-for-java/latest/developer-guide/using.html">
 *     Use the AWS SDK for Java 2.x</a>
 * @see software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider
 * @see software.amazon.awssdk.regions.providers.SystemSettingsRegionProvider
 */
class StorageModule : AbstractModule() {
    @Provides
    @Singleton
    fun storageProperties(): StorageProperties =
        StorageProperties(
            System.getenv(REGION_PROP.envName),
            System.getenv(ACCESS_KEY_ID_PROP.envName),
            System.getenv(SECRET_ACCESS_KEY_PROP.envName),
            System.getenv(API_URL_PROP.envName),
            System.getenv(BASE_LOCATION_PROP.envName)
        )
            .mapLeft(Failure::toThrowable)
            .onLeft { throw InitializationError(it) }
            .getOrNull()!!

    @Provides
    @Singleton
    @Named("minioServiceClient")
    fun minioServiceClient(properties: StorageProperties): S3Client {
        logger.info { "Creating buckets storage HTTP client with $properties" }
        val (region, accessKeyId, secretAccessKey, apiURL) = properties
        val bucketInHostnameDisabled = true
        return S3Client.builder()
            .credentialsProvider { AwsBasicCredentials.create(accessKeyId, secretAccessKey) }
            .region(Region.of(region))
            .httpClient(UrlConnectionHttpClient.builder().build())
            .endpointOverride(URI.create(apiURL))
            .forcePathStyle(bucketInHostnameDisabled)
            .build()
    }

    @Provides
    @Singleton
    @Named("bucketsStorageService")
    fun bucketsStorageService(
        properties: StorageProperties,
        @Named("minioServiceClient") delegate: S3Client
    ): StorageService = BucketsStorageService(properties, delegate)

    @UploaderMapKey(StorageType.BUCKETS)
    @ProvidesIntoMap
    @Singleton
    fun bucketsStorageUploader(
        parser: JSONParser,
        @Named("bucketsStorageService") service: StorageService
    ): StorageUploader = BucketsStorageUploader(parser, service)

    @Provides
    @Singleton
    fun fileSystemProperties(): FileSystemProperties =
        FileSystemProperties(
            System.getenv(BASE_DIRECTORY_PROP.envName)
        )
            .mapLeft(Failure::toThrowable)
            .onLeft { throw InitializationError(it) }
            .getOrNull()!!

    @Provides
    @Singleton
    fun fileSystemService(
        properties: FileSystemProperties
    ): FileSystemService = FileSystemService(properties)

    @UploaderMapKey(StorageType.FILE_SYSTEM)
    @ProvidesIntoMap
    @Singleton
    fun fileSystemStorageUploader(
        parser: JSONParser,
        service: FileSystemService
    ): StorageUploader = FileSystemStorageUploader(parser, service)
}

@MapKey(unwrapValue = true)
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class UploaderMapKey(val value: StorageType)
