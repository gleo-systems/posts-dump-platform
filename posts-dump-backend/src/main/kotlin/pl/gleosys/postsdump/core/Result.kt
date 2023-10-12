package pl.gleosys.postsdump.core

object Success

sealed class Failure(open val cause: Throwable? = null, open val message: String? = null) {
    fun toThrowable() = Throwable(this.message, this.cause)

    companion object FailureFactory {
        @JvmStatic
        inline fun <reified T : Failure> newInstance(t: Throwable) =
            T::class.java.getConstructor(Throwable::class.java, String::class.java)
                .newInstance(t, t.message)
    }

    data class ApplicationError(
        override val cause: Throwable? = null,
        override val message: String? = null
    ) : Failure(cause, message)

    data class InfrastructureError(
        override val cause: Throwable? = null,
        override val message: String? = null
    ) : Failure(cause, message)

    data class ValidationError(
        override val message: String? = null,
        override val cause: Throwable? = null
    ) : Failure(cause, message)

    data class ConversionError(
        override val cause: Throwable? = null,
        override val message: String? = null
    ) : Failure(cause, message)
}
