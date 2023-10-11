package pl.gleosys.postsdump.infrastructure.messagebroker

data class MessageBrokerProperties(
    val username: String,
    val password: String,
    val hostName: String,
    val consumerTag: String,
    val channelName: String,
    val channelAutoAck: Boolean
) {
    override fun toString() =
        "ConnectionProperties(" +
            "username=$username, " +
            "password=###, " +
            "hostName=$hostName, " +
            "consumerTag=$consumerTag, " +
            "channelName=$channelName, " +
            "channelAutoAck=$channelAutoAck)"
}
