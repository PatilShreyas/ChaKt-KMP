package util

import java.awt.Toolkit
import java.awt.datatransfer.DataFlavor

/**
 * Platform's context.
 */
actual object PlatformContext

actual val platformContext: PlatformContext = PlatformContext

actual fun PlatformContext.getClipboardText(): String? {
    return Toolkit.getDefaultToolkit().systemClipboard.getData(DataFlavor.stringFlavor) as? String
}