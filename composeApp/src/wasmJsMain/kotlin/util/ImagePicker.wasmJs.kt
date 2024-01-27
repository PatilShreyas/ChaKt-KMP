package util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import kotlinx.browser.document
import org.jetbrains.skia.Image
import org.khronos.webgl.ArrayBuffer
import org.khronos.webgl.Uint8Array
import org.khronos.webgl.get
import org.w3c.dom.HTMLInputElement
import org.w3c.files.FileReader
import org.w3c.files.get
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

@Composable
actual fun ImagePicker(onResult: (ByteArray?) -> Unit) {
    LaunchedEffect(Unit) {
        val data = runCatching { importImage() }.getOrNull()
        onResult(data)
    }
}

private suspend fun importImage(): ByteArray? = suspendCoroutine { cont ->
    try {
        val input = document.createElement("input").apply {
            setAttribute("type", "file")
            setAttribute("accept", "image/*")
        } as HTMLInputElement

        input.onchange = {
            val file = input.files?.get(0)
            if (file != null) {
                val reader = FileReader()
                reader.onload = { event ->
                    val arrayBuffer = (event.target as FileReader).result as ArrayBuffer
                    val array = Uint8Array(arrayBuffer)

                    cont.resume(ByteArray(array.length) { array[it] })
                }
                reader.onerror = {
                    cont.resumeWithException(Exception(reader.error.toString()))
                }
                reader.readAsArrayBuffer(file)
            } else {
                cont.resumeWithException(Exception("No file was selected"))
            }
        }
        input.click()
    } catch (e: Exception) {
        cont.resumeWithException(e)
    }
}

actual fun ByteArray.toComposeImageBitmap(): ImageBitmap {
    return Image.makeFromEncoded(this).toComposeImageBitmap()
}