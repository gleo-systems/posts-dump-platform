package pl.gleosys.postsdump.infrastructure.rabbitmq

data class ConnectionProperties(
    val username: String,
    val password: String,
    val hostName: String,
    val consumerTag: String,
    val queueName: String,
    val queueAutoAck: Boolean,
) {
    override fun toString(): String {
        return "ConnectionProperties(" +
            "username=$username, " +
            "password=###, " +
            "hostName=$hostName, " +
            "consumerTag=$consumerTag, " +
            "queueName=$queueName, " +
            "queueAutoAck=$queueAutoAck)"
    }
}
