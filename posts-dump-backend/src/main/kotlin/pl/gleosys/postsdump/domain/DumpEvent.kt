package pl.gleosys.postsdump.domain

import java.util.*

enum class StorageType(val value: String) {
    BUCKETS("BUCKETS"),
    FILE_SYSTEM("FILE_SYSTEM")
}

data class DumpEvent(val id: UUID, val storageType: StorageType)
