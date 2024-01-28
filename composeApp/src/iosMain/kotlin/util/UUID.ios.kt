package util

import platform.Foundation.NSUUID.Companion.UUID

actual fun getUUIDString(): String {
    return UUID().UUIDString
}