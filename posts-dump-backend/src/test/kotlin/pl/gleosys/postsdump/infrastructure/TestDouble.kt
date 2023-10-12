package pl.gleosys.postsdump.infrastructure

import pl.gleosys.postsdump.infrastructure.storage.StorageProperties

object TestDouble {
    @JvmField
    val STORAGE_PROPERTIES = StorageProperties(
        "region",
        "keyId",
        "accessKey",
        "apiURL",
        "baseLocation"
    ).getOrNull()!!
}
