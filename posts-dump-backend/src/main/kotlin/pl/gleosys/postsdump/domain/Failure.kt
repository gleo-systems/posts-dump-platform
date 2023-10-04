package pl.gleosys.postsdump.domain

// TODO: consider sealed class
open class Failure(open val cause: Throwable? = null, open val message: String? = null)

fun Failure.toThrowable() = Throwable(this.message, this.cause)
