import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Login
import androidx.compose.material.icons.filled.ContentPasteGo
import androidx.compose.material.icons.filled.Key
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import service.GenerativeAiService
import ui.screen.ChatScreen
import util.isValidApiKey
import util.rememberClipboardManager

/**
 * Entry point of application
 */
@Composable
fun App() {
    MaterialTheme {
        var isAiServiceInitialized by remember { mutableStateOf(GenerativeAiService.GEMINI_API_KEY.isNotBlank()) }

        if (isAiServiceInitialized) {
            ChatScreen()
        } else {
            SetApiKeyDialog(onAiServiceInitialized = { isAiServiceInitialized = true })
        }
    }
}

@Composable
fun SetApiKeyDialog(onAiServiceInitialized: () -> Unit) {
    var apiKey by rememberSaveable { mutableStateOf("") }
    var isValidApiKey by remember { mutableStateOf(false) }
    var isApiValidFromKeyboard by remember { mutableStateOf(true) }
    val clipboardManager = rememberClipboardManager()
    val coroutineScope = rememberCoroutineScope()

    Dialog(onDismissRequest = {}) {
        Surface {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(16.dp)
            ) {
                Text("Set Gemini API key to enter Chat")
                OutlinedTextField(
                    value = apiKey,
                    onValueChange = {
                        apiKey = it
                    },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("API Key") },
                    leadingIcon = { Icon(Icons.Default.Key, "Key icon") },
                    trailingIcon = {
                        IconButton(
                            enabled = isValidApiKey,
                            onClick = {
                                GenerativeAiService.GEMINI_API_KEY = apiKey
                                onAiServiceInitialized()
                            }
                        ) {
                            Icon(Icons.AutoMirrored.Filled.Login, "Key icon")
                        }
                    },
                    isError = !isValidApiKey,
                    singleLine = true,
                    supportingText = {
                        if (!isValidApiKey) {
                            Text(
                                text = "Place valid Gemini API key here",
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    },
                )

                OutlinedButton(onClick = {
                    coroutineScope.launch {
                        val key = clipboardManager.getClipboardText()
                        val isValidKey = key?.let { isValidApiKey(it) } ?: false

                        if (isValidKey) {
                            apiKey = key!!
                            isValidApiKey = true
                        } else {
                            isApiValidFromKeyboard = false
                            delay(3000)
                            isApiValidFromKeyboard = true
                        }
                    }
                }) {
                    Icon(Icons.Filled.ContentPasteGo, "Copy")
                    Text("Copy from clipboard")
                }

                AnimatedVisibility(!isApiValidFromKeyboard) {
                    Box(
                        Modifier
                            .padding(8.dp)
                            .background(MaterialTheme.colorScheme.errorContainer)
                            .clip(RoundedCornerShape(4.dp))
                            .padding(8.dp)
                    ) {
                        Text(
                            "Clipboard does not contains valid Gemini API key",
                            color = MaterialTheme.colorScheme.onErrorContainer
                        )
                    }
                }
            }
        }
    }

    LaunchedEffect(apiKey) {
        isValidApiKey = isValidApiKey(apiKey)
    }
}