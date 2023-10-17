package pl.gleosys.postsdump.core

class InitializationError(
    override val cause: Throwable? = null,
    override val message: String? = null
) : Error(message, cause)
