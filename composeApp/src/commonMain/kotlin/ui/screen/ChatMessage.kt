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

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.ImageBitmap
import kotlinx.coroutines.flow.Flow
import util.getUUIDString

/**
 * Represents as a Chat message model
 */
@Immutable
sealed interface ChatMessage {

    val id: String

    fun isUserMessage(): Boolean {
        return this is UserChatMessage
    }

    fun isModelMessage(): Boolean {
        return this is ModelChatMessage
    }

    fun isErrorMessage(): Boolean {
        return this is ModelChatMessage.ErrorMessage
    }
}

/**
 * User prompt message model
 */
@Immutable
data class UserChatMessage(
    val text: String,
    val image: ImageBitmap?,
    override val id: String = "user-${getUUIDString()}",
) : ChatMessage

/**
 * Model's response chat model
 */
@Immutable
sealed interface ModelChatMessage : ChatMessage {

    /**
     * Represents Loading state of a model message
     */
    @Immutable
    data class LoadingModelMessage(
        val textStream: Flow<String>,
        override val id: String = "modelloading-${getUUIDString()}",
    ) : ModelChatMessage

    /**
     * Represents loaded state of a model message
     */
    @Immutable
    data class LoadedModelMessage(
        val text: String,
        override val id: String = "model-${getUUIDString()}",
    ) : ModelChatMessage

    /**
     * Represents error state of a model message
     */
    @Immutable
    data class ErrorMessage(
        val text: String,
        override val id: String = "error-${getUUIDString()}",
    ) : ChatMessage
}
