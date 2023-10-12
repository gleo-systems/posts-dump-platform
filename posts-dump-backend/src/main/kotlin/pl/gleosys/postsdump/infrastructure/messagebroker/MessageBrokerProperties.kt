package pl.gleosys.postsdump.infrastructure.messagebroker

import arrow.core.Either
import arrow.core.raise.either
import arrow.core.raise.ensure
import pl.gleosys.postsdump.core.Failure.ValidationError

data class MessageBrokerProperties private constructor(
    val username: String,
    val password: String,
    val hostName: String,
    val consumerTag: String,
    val channelName: String,
    val channelAutoACK: Boolean
) {
    companion object {
        operator fun invoke(
            username: String,
            password: String,
            hostName: String,
            consumerTag: String,
            channelName: String,
            channelAutoAck: Boolean
        ): Either<ValidationError, MessageBrokerProperties> {
            return either {
                ensure(username.isNotBlank()) { ValidationError("Empty username value") }
                ensure(password.isNotBlank()) { ValidationError("Empty password value") }
                ensure(hostName.isNotBlank()) { ValidationError("Empty hostName value") }
                ensure(consumerTag.isNotBlank()) { ValidationError("Empty consumerTag value") }
                ensure(channelName.isNotBlank()) { ValidationError("Empty channelName value") }
                // channelAutoACK boolean check is not needed
                MessageBrokerProperties(
                    username,
                    password,
                    hostName,
                    consumerTag,
                    channelName,
                    channelAutoAck
                )
            }
        }
    }

    override fun toString() =
        "ConnectionProperties(" +
            "username=$username, " +
            "password=###, " +
            "hostName=$hostName, " +
            "consumerTag=$consumerTag, " +
            "channelName=$channelName, " +
            "channelAutoACK=$channelAutoACK)"
}
