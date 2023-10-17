package pl.gleosys.postsdump.core

object Success

/**
 * Requires instance constructor(String, Throwable)
 */
sealed class Failure(open val cause: Throwable? = null, open val message: String? = null) {
    fun toThrowable() = Throwable(this.message, this.cause)

    companion object FailureFactory {
        @JvmStatic
        inline fun <reified T : Failure> newInstance(t: Throwable): T =
            T::class.java.getConstructor(String::class.java, Throwable::class.java)
                .newInstance(t.message, t)
    }

    data class ApplicationError(
        override val message: String? = null,
        override val cause: Throwable? = null
    ) : Failure(cause, message)

    data class InfrastructureError(
        override val message: String? = null,
        override val cause: Throwable? = null
    ) : Failure(cause, message)

    data class ValidationError(
        override val message: String? = null,
        override val cause: Throwable? = null
    ) : Failure(cause, message)

    data class ConversionError(
        override val message: String? = null,
        override val cause: Throwable? = null
    ) : Failure(cause, message)
}
