package pl.gleosys.postsdump.domain

// TODO: consider sealed class
open class Failure(open val message: String? = null, open val cause: Throwable? = null)

fun Failure.toThrowable() = Throwable(this.message, this.cause)
