package util

actual fun getUUIDString(): String {
    return runCatching { getUUIDOrNull() }.getOrNull()?.toString() ?: getDate().toString()
}

fun getUUIDOrNull(): JsAny = js("window.crypto.randomUUID()")
fun getDate(): JsAny = js("Date.now()")