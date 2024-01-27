package ui.screen

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

/**
 * UI state model for Chat listing screen
 */
@Stable
interface ChatUiState {
    val messages: List<ChatMessage>
    val canSendMessage: Boolean
}

/**
 * Mutable state implementation for [ChatUiState]
 */
class MutableChatUiState : ChatUiState {
    override val messages = mutableStateListOf<ChatMessage>()

    override var canSendMessage: Boolean by mutableStateOf(true)

    fun addMessage(message: ChatMessage) {
        messages.add(message)
    }

    fun setLastModelMessageAsLoaded(text: String) {
        updateLastModelMessage { ModelChatMessage.LoadedModelMessage(text) }
    }

    fun setLastMessageAsError(error: String) {
        updateLastModelMessage { ModelChatMessage.ErrorMessage(error) }
    }

    private fun updateLastModelMessage(block: (ModelChatMessage) -> ChatMessage) {
        val lastMessage = messages.lastOrNull() as? ModelChatMessage
        lastMessage?.let {
            val newMessage = block(it)
            messages.removeLast()
            messages.add(newMessage)
        }
    }
}
