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
    override val id: String = "user-${getUUIDString()}"
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
        override val id: String = "modelloading-${getUUIDString()}"
    ) : ModelChatMessage

    /**
     * Represents loaded state of a model message
     */
    @Immutable
    data class LoadedModelMessage(
        val text: String,
        override val id: String = "model-${getUUIDString()}"
    ) : ModelChatMessage

    /**
     * Represents error state of a model message
     */
    @Immutable
    data class ErrorMessage(
        val text: String,
        override val id: String = "error-${getUUIDString()}"
    ) : ChatMessage
}