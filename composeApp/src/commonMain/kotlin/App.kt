import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Login
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import org.jetbrains.compose.resources.ExperimentalResourceApi
import service.GenerativeAiService
import ui.screen.ChatScreen
import util.isValidApiKey

/**
 * Entry point of application
 */
@OptIn(ExperimentalResourceApi::class)
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

    Dialog(onDismissRequest = {}) {
        Surface {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .padding(16.dp)
            ) {
                Text("Set Gemini API key to enter Chat")
                OutlinedTextField(
                    value = apiKey,
                    onValueChange = {
                        apiKey = it
                        isValidApiKey = isValidApiKey(it)
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
                            Icon(Icons.Default.Login, "Key icon")
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
            }
        }
    }
}