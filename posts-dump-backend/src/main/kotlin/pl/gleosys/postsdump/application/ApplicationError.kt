package pl.gleosys.postsdump.application

import pl.gleosys.postsdump.domain.Failure

data class ApplicationError(
    override val cause: Throwable? = null,
    override val message: String? = null,
) : Failure(cause, message)
