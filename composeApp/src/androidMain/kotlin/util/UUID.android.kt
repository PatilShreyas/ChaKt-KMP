package util

import java.util.UUID

actual fun getUUIDString(): String {
    return UUID.randomUUID().toString()
}