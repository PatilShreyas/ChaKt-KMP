package ui.screen

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.ImageBitmap
import kotlinx.coroutines.flow.Flow

/**
 * Represents as a Chat message model
 */
@Immutable
sealed interface ChatMessage {

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
data class UserChatMessage(val text: String, val image: ImageBitmap?) : ChatMessage

/**
 * Model's response chat model
 */
@Immutable
sealed interface ModelChatMessage : ChatMessage {

    /**
     * Represents Loading state of a model message
     */
    @Immutable
    data class LoadingModelMessage(val textStream: Flow<String>) : ModelChatMessage

    /**
     * Represents loaded state of a model message
     */
    @Immutable
    data class LoadedModelMessage(val text: String) : ModelChatMessage

    /**
     * Represents error state of a model message
     */
    @Immutable
    data class ErrorMessage(val text: String) : ChatMessage
}