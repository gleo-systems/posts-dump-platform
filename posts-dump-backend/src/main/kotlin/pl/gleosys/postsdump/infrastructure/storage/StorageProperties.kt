package pl.gleosys.postsdump.infrastructure.storage

data class StorageProperties(
    val region: String,
    val accessKeyId: String,
    val secretAccessKey: String,
    val apiURL: String,
    val baseLocation: String
) {
    override fun toString() =
        "StorageProperties(" +
            "region=$region, " +
            "accessKeyId=$accessKeyId, " +
            "secretAccessKey=###,  " +
            "apiURL=$apiURL, " +
            "baseLocation=$baseLocation)"
}
