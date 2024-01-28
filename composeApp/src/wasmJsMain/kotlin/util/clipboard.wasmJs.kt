package util

import org.w3c.dom.clipboard.Clipboard
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

/**
 * Platform's context.
 */
actual object PlatformContext

actual val platformContext: PlatformContext = PlatformContext

actual suspend fun PlatformContext.getClipboardText(): String? = suspendCoroutine { cont ->
    getClipboard().readText().then { content ->
        content.also { cont.resume(content.toString()) }
    }
}

fun getClipboard(): Clipboard = js("navigator.clipboard")