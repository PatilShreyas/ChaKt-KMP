package service

import dev.shreyaspatil.ai.client.generativeai.Chat
import dev.shreyaspatil.ai.client.generativeai.GenerativeModel
import dev.shreyaspatil.ai.client.generativeai.type.Content
import dev.shreyaspatil.ai.client.generativeai.type.GenerateContentResponse
import dev.shreyaspatil.ai.client.generativeai.type.PlatformImage
import dev.shreyaspatil.ai.client.generativeai.type.content
import dev.shreyaspatil.chakt.BuildKonfig
import kotlinx.coroutines.flow.Flow

/**
 * Service for Generative AI operations that can interact with text as well as images.
 */
class GenerativeAiService private constructor(
    private val textModel: GenerativeModel,
    private val visionModel: GenerativeModel,
) {

    /**
     * Creates a chat instance which internally tracks the ongoing conversation with the model
     *
     * @param history History of conversation
     */
    fun startChat(history: List<Content>): Chat {
        return textModel.startChat(history)
    }

    /**
     * Generates a streaming response from the backend with the provided [prompt] and [image].
     *
     * @param prompt A user prompt
     * @param image Image data as bytes
     */
    fun generateContentWithVision(prompt: String, image: ByteArray): Flow<GenerateContentResponse> {
        val content = content {
            image(PlatformImage(image))
            text(prompt)
        }
        return visionModel.generateContentStream(content)
    }

    companion object {
        var GEMINI_API_KEY = BuildKonfig.GEMINI_API_KEY

        val instance: GenerativeAiService by lazy {
            GenerativeAiService(
                textModel = GenerativeModel(
                    modelName = "gemini-pro",
                    apiKey = GEMINI_API_KEY
                ),
                visionModel = GenerativeModel(
                    modelName = "gemini-pro-vision",
                    apiKey = GEMINI_API_KEY
                )
            )
        }
    }
}