package pl.gleosys.postsdump.application

import pl.gleosys.postsdump.domain.Failure

data class ApplicationError(
    override val message: String? = null,
    override val cause: Throwable? = null,
) : Failure(message, cause)
