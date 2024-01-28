package util

import org.w3c.dom.Navigator

/**
 * Platform's context.
 */
actual object PlatformContext

actual val platformContext: PlatformContext = PlatformContext

actual fun PlatformContext.getClipboardText(): String? {
    return navigator.clipboard.readText().toString()
}

private val navigator: Navigator get() = js("navigator")