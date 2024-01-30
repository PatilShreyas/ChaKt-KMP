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
package ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Send
import androidx.compose.material.icons.rounded.AttachFile
import androidx.compose.material.icons.rounded.Cancel
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import util.ImagePicker
import util.toComposeImageBitmap

@Composable
fun MessageInput(
    enabled: Boolean,
    onSendMessage: (prompt: String, image: ByteArray?) -> Unit,
) {
    var userMessage by rememberSaveable { mutableStateOf("") }
    var selectedImage by remember { mutableStateOf<ByteArray?>(null) }
    var selectedImageBitmap by remember { mutableStateOf<ImageBitmap?>(null) }
    var showImagePicker by remember { mutableStateOf(false) }

    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth(),
    ) {
        Column {
            selectedImageBitmap?.let { image ->
                Box(Modifier.size(96.dp).padding(vertical = 4.dp, horizontal = 16.dp)) {
                    Image(
                        bitmap = image,
                        contentDescription = "Selected image",
                        modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(8.dp)),
                        contentScale = ContentScale.Crop,
                    )
                    IconButton(
                        onClick = { selectedImage = null },
                        modifier = Modifier.size(48.dp).align(Alignment.TopEnd),
                    ) {
                        Icon(Icons.Rounded.Cancel, "Remove attached Image File")
                    }
                }
            }
            Row(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                OutlinedTextField(
                    value = userMessage,
                    label = { Text("Talk to AI...") },
                    onValueChange = { userMessage = it },
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Sentences,
                    ),
                    leadingIcon = if (selectedImageBitmap == null) {
                        {
                            IconButton(onClick = { showImagePicker = true }) {
                                Icon(Icons.Rounded.AttachFile, "Attach Image File")
                            }
                        }
                    } else {
                        null
                    },
                    trailingIcon = {
                        IconButton(
                            enabled = enabled,
                            onClick = {
                                if (userMessage.isNotBlank()) {
                                    onSendMessage(userMessage, selectedImage)
                                    userMessage = ""
                                    selectedImage = null
                                }
                            },
                        ) {
                            Icon(
                                Icons.AutoMirrored.Rounded.Send,
                                contentDescription = "Send message",
                            )
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }

    ImagePicker(showImagePicker) {
            selectedImage = it
            showImagePicker = false
    }

    LaunchedEffect(selectedImage) {
        withContext(Dispatchers.Default) {
            selectedImageBitmap = selectedImage?.toComposeImageBitmap()
        }
    }
}
