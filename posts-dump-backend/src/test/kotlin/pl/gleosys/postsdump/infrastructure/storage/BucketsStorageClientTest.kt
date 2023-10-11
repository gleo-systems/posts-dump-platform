package pl.gleosys.postsdump.infrastructure.storage

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.equals.shouldBeEqual
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.spyk
import io.mockk.verify
import pl.gleosys.postsdump.infrastructure.TestDouble
import software.amazon.awssdk.awscore.exception.AwsServiceException
import software.amazon.awssdk.core.sync.RequestBody
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.PutObjectRequest
import software.amazon.awssdk.services.s3.model.PutObjectResponse
import java.nio.file.Path

class BucketsStorageClientTest : BehaviorSpec({
    val propertiesSTUB = TestDouble.STORAGE_PROPERTIES
    val destinationSTUB = Path.of("/destination")
    val contentSTUB = "content".toByteArray()
    val putObjectResponseSTUB = PutObjectResponse.builder().build()

    val delegateSPY = spyk<S3Client>()

    val client = BucketsStorageClient(propertiesSTUB, delegateSPY)

    beforeTest {
        every {
            delegateSPY.putObject(
                any(PutObjectRequest::class),
                any(RequestBody::class)
            )
        } returns putObjectResponseSTUB
    }

    given("data to be stored") {

        `when`("uploading content to specified bucket storage destination") {
            then("return success") {
                val request = PutObjectRequest.builder()
                    .bucket(propertiesSTUB.baseLocation)
                    .key(destinationSTUB.toString())
                    .build()

                val failureOpt = client.upload(destinationSTUB, contentSTUB)

                failureOpt.isSome() shouldBe false
                verify {
                    delegateSPY.putObject(
                        withArg<PutObjectRequest> { it shouldBeEqual request },
                        withArg<RequestBody> {
                            it.contentStreamProvider().newStream()
                                .readAllBytes() shouldBe contentSTUB
                        }
                    )
                }
            }
        }

        `when`("uploading content and receiving storage error") {
            then("return failure") {
                val errorMsg = "Error"
                every {
                    delegateSPY.putObject(
                        any(PutObjectRequest::class),
                        any(RequestBody::class)
                    )
                } throws AwsServiceException.builder().message(errorMsg).build()

                val failureOpt = client.upload(destinationSTUB, contentSTUB)

                failureOpt.should {
                    it.isSome() shouldBeEqual true
                    it.getOrNull()!!.message!! shouldBeEqual errorMsg
                }
            }
        }

        xwhen("uploading content and passing invalid parameters") {
            then("return failure", TODO())
        }
    }
})
