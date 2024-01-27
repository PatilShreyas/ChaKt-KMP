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
import androidx.compose.material.icons.rounded.AttachFile
import androidx.compose.material.icons.rounded.Cancel
import androidx.compose.material.icons.rounded.Send
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
    resetScroll: () -> Unit = {}
) {
    var userMessage by rememberSaveable { mutableStateOf("") }
    var selectedImage by remember { mutableStateOf<ByteArray?>(null) }
    var selectedImageBitmap by remember { mutableStateOf<ImageBitmap?>(null) }
    var showImagePicker by remember { mutableStateOf(false) }

    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Column {
            selectedImageBitmap?.let { image ->
                Box(Modifier.size(96.dp).padding(vertical = 4.dp, horizontal = 16.dp)) {
                    Image(
                        bitmap = image,
                        contentDescription = "Selected image",
                        modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(8.dp)),
                        contentScale = ContentScale.Crop
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
                verticalAlignment = Alignment.CenterVertically
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
                    } else null,
                    trailingIcon = {
                        IconButton(
                            enabled = enabled,
                            onClick = {
                                if (userMessage.isNotBlank()) {
                                    onSendMessage(userMessage, selectedImage)
                                    userMessage = ""
                                    selectedImage = null
                                    resetScroll()
                                }
                            }
                        ) {
                            Icon(
                                Icons.Rounded.Send,
                                contentDescription = "Send message",
                            )
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }

    if (showImagePicker) {
        ImagePicker {
            selectedImage = it
            showImagePicker = false
        }
    }

    LaunchedEffect(selectedImage) {
        withContext(Dispatchers.Default) {
            selectedImageBitmap = selectedImage?.toComposeImageBitmap()
        }
    }
}