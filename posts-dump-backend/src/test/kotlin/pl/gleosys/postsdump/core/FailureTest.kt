package pl.gleosys.postsdump.core

import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.equals.shouldBeEqual
import pl.gleosys.postsdump.core.Failure.ApplicationError
import pl.gleosys.postsdump.core.Failure.ConversionError
import pl.gleosys.postsdump.core.Failure.FailureFactory
import pl.gleosys.postsdump.core.Failure.InfrastructureError
import pl.gleosys.postsdump.core.Failure.ValidationError

class FailureFactoryTest : BehaviorSpec({
    val errorMsg = "Throwable"
    val cause = Throwable("Throwable")
    val exc = Exception(errorMsg, cause)

    given("business logic exception") {

        `when`("trying to convert to Failure subtype") {
            /**
             * TODO: use withData
             * @see pl.gleosys.postsdump.core.StringTest
             */
            then("return valid object") {

                FailureFactory.newInstance<ApplicationError>(exc) shouldBeEqual
                    ApplicationError(errorMsg, exc)

                var e = Exception(errorMsg)
                FailureFactory.newInstance<InfrastructureError>(e) shouldBeEqual
                    InfrastructureError(errorMsg, e)

                e = Exception(cause)
                FailureFactory.newInstance<ValidationError>(e) shouldBeEqual
                    ValidationError("java.lang.Throwable: Throwable", e)

                e = Exception()
                FailureFactory.newInstance<ConversionError>(e) shouldBeEqual
                    ConversionError(cause = e)
            }
        }


        `when`("trying to convert to Failure instance") {
            /**
             * TODO: use withData
             * @see pl.gleosys.postsdump.core.StringTest
             */
            then("throw NoSuchMethodException") {
                shouldThrowExactly<NoSuchMethodException> {
                    FailureFactory.newInstance<Failure>(exc)
                }
            }
        }
    }
})
