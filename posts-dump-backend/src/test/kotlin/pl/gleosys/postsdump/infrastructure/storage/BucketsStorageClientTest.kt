package pl.gleosys.postsdump.infrastructure.storage

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.equals.shouldBeEqual
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.mockk.called
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

    val client = BucketsStorageService(propertiesSTUB, delegateSPY)

    beforeTest {
        every {
            delegateSPY.putObject(
                any(PutObjectRequest::class),
                any(RequestBody::class)
            )
        } returns putObjectResponseSTUB
    }

    given("data to be stored") {

        `when`("uploading content to specified bucket destination") {
            then("return success") {
                val request = PutObjectRequest.builder()
                    .bucket(propertiesSTUB.baseLocation)
                    .key(destinationSTUB.toString())
                    .build()

                val result = client.uploadData(destinationSTUB, contentSTUB)

                result.isRight() shouldBeEqual true
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
                val errorMsg = "AwsServiceException"
                every {
                    delegateSPY.putObject(
                        any(PutObjectRequest::class),
                        any(RequestBody::class)
                    )
                } throws AwsServiceException.builder().message(errorMsg).build()

                val result = client.uploadData(destinationSTUB, contentSTUB)

                result.should {
                    it.isLeft() shouldBeEqual true
                    it.leftOrNull()!!.message!! shouldBeEqual errorMsg
                }
            }
        }

        `when`("uploading content and passing empty destination") {
            then("return failure") {
                val result = client.uploadData(Path.of(""), contentSTUB)

                result.should {
                    it.isLeft() shouldBeEqual true
                    it.leftOrNull()!! should { err ->
                        err.cause!!::class shouldBeEqual IllegalArgumentException::class
                        err.message!! shouldBeEqual "Failed requirement."
                    }
                }
                verify {
                    delegateSPY.putObject(any<PutObjectRequest>(), any<RequestBody>()) wasNot called
                }
            }
        }

        `when`("uploading content and passing empty content") {
            then("return failure") {
                val result = client.uploadData(destinationSTUB, "".toByteArray())

                result.should {
                    it.isLeft() shouldBeEqual true
                    it.leftOrNull()!! should { err ->
                        err.cause!!::class shouldBeEqual IllegalArgumentException::class
                        err.message!! shouldBeEqual "Failed requirement."
                    }
                }
                verify {
                    delegateSPY.putObject(any<PutObjectRequest>(), any<RequestBody>()) wasNot called
                }
            }
        }
    }
})
