package pl.gleosys.postsdump.infrastructure

import pl.gleosys.postsdump.domain.Failure

data class InfrastructureError(
    override val message: String? = null,
    override val cause: Throwable? = null,
) : Failure(message, cause)
