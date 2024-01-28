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
