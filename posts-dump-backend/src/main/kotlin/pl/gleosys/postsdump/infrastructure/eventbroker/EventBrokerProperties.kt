package pl.gleosys.postsdump.infrastructure.eventbroker

import arrow.core.Either
import arrow.core.raise.either
import arrow.core.raise.ensure
import pl.gleosys.postsdump.core.Failure.ValidationError

class EventBrokerProperties private constructor(
    val notificationsTopicName: String,
    val apiURL: String
) {
    companion object {
        operator fun invoke(
            notificationsTopicName: String,
            apiURL: String
        ): Either<ValidationError, EventBrokerProperties> {
            return either {
                ensure(notificationsTopicName.isNotBlank()) { ValidationError("Empty notificationsTopicName value") }
                ensure(apiURL.isNotBlank()) { ValidationError("Empty apiURL value") }
                EventBrokerProperties(notificationsTopicName, apiURL)
            }
        }
    }
}
