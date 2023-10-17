package pl.gleosys.postsdump.util

class InitializationError(
    override val cause: Throwable? = null,
    override val message: String? = null
) : Error(message, cause)
