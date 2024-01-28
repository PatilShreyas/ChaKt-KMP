package util

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.platform.LocalContext

/**
 * Platform's context.
 */
typealias PlatformContext = Context

actual val platformContext: PlatformContext
    @Composable
    @ReadOnlyComposable
    get() = LocalContext.current

actual fun PlatformContext.getClipboardText(): String? {
    val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
    val clip = clipboard.primaryClip
    if (clip != null && clip.itemCount > 0) {
        val item = clip.getItemAt(0)
        return item.text.toString()
    }
    return null
}
