package ui.screen

import dev.shreyaspatil.ai.client.generativeai.type.PlatformImage
import dev.shreyaspatil.ai.client.generativeai.type.content
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import service.GenerativeAiService
import util.toComposeImageBitmap

class ChatViewModel(private val aiService: GenerativeAiService) {
    private val coroutineScope = MainScope()

    private val chat = aiService.startChat(
        history = listOf(
            content(role = "user") { text("Hello AI.") },
            content(role = "model") { text("Great to meet you. What would you like to know?") }
        )
    )

    private val _uiState = MutableChatUiState()
    val uiState: ChatUiState = _uiState

    fun sendMessage(prompt: String, image: ByteArray?) {
        val completeText = StringBuilder()

        val base = if (image != null) {
            aiService.generateContentWithVision(prompt, image)
        } else {
            chat.sendMessageStream(prompt)
        }

        val modelMessage = ModelChatMessage.LoadingModelMessage(
            base.map { it.text ?: "" }
                .onEach { completeText.append(it) }
                .onStart { _uiState.canSendMessage = false }
                .onCompletion {
                    _uiState.setLastModelMessageAsLoaded(completeText.toString())
                    _uiState.canSendMessage = true

                    if (image != null) {
                        chat.history.add(content("user") { text(prompt) })
                        chat.history.add(content("model") { text(completeText.toString()) })
                    }
                }
                .catch { _uiState.setLastMessageAsError(it.toString()) }
        )

        coroutineScope.launch(Dispatchers.Default) {
            _uiState.addMessage(UserChatMessage(prompt, image?.toComposeImageBitmap()))
            _uiState.addMessage(modelMessage)
        }
    }

    fun onCleared() {
        println("ChatViewModel: onCleared")
        coroutineScope.cancel()
    }
}