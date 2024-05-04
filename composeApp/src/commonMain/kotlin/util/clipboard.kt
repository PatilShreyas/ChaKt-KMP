/*
 * MIT License
 *
 * Copyright (c) 2024 Shreyas Patil
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.remember
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Platform's context.
 */
expect class PlatformContext

@get:Composable
@get:ReadOnlyComposable
expect val platformContext: PlatformContext

/**
 * Clipboard manager for the platform that has capability to read the text from clipboard.
 */
class ClipboardManager(private val platformContext: PlatformContext) {
    /**
     * Returns the text from the clipboard.
     */
    suspend fun getClipboardText(): String? = withContext(Dispatchers.Default) {
        platformContext.getClipboardText()
    }
}

/**
 * Returns the [ClipboardManager] for the current platform in the current composition.
 */
@Composable
fun rememberClipboardManager(): ClipboardManager {
    val context = platformContext
    return remember { ClipboardManager(context) }
}

expect suspend fun PlatformContext.getClipboardText(): String?
