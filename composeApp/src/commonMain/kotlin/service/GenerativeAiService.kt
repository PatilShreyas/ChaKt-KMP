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
        @Suppress("ktlint:standard:property-naming")
        var GEMINI_API_KEY = BuildKonfig.GEMINI_API_KEY

        val instance: GenerativeAiService by lazy {
            GenerativeAiService(
                textModel = GenerativeModel(
                    modelName = "gemini-pro",
                    apiKey = GEMINI_API_KEY,
                ),
                visionModel = GenerativeModel(
                    modelName = "gemini-pro-vision",
                    apiKey = GEMINI_API_KEY,
                ),
            )
        }
    }
}
