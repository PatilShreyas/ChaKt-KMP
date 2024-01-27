package util

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.ImageBitmap

/**
 * Picks image file from platform's system and gives file data in [onResult]
 *
 * @param onResult Will be invoked when file is picked. Will be invoked with `null` when file is
 * not picked
 */
@Composable
expect fun ImagePicker(onResult: (ByteArray?) -> Unit)

/**
 * Creates [ImageBitmap] from this [ByteArray]
 */
expect fun ByteArray.toComposeImageBitmap(): ImageBitmap