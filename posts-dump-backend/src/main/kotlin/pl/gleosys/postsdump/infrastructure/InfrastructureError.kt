package pl.gleosys.postsdump.infrastructure

import pl.gleosys.postsdump.domain.Failure

data class InfrastructureError(
    override val cause: Throwable? = null,
    override val message: String? = null,
) : Failure(cause, message)
