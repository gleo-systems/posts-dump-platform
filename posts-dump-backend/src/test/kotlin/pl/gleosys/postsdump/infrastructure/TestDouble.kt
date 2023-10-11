package pl.gleosys.postsdump.infrastructure

import pl.gleosys.postsdump.infrastructure.storage.StorageProperties

class TestDouble {
    companion object {
        @JvmStatic
        val STORAGE_PROPERTIES = StorageProperties(
            "region",
            "keyId",
            "accessKey",
            "apiURL",
            "baseLocation"
        )
    }
}
