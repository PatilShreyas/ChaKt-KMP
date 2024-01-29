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
package ui.component

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.unit.em
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.fold
import kotlinx.coroutines.flow.map

private const val LOADER_ALT_TEXT = "[LOADER]"
private const val LOADER_INLINE_ID = "loader"

/**
 * Animated the text as a loading effect from the [stream] of text.
 *
 * @param modifier Modifier to be applied on to the text
 * @param stream Stream of text
 * @param onLoaded Invoked whenever the text is completely loaded
 */
@Composable
fun LoadingText(
    modifier: Modifier = Modifier,
    stream: Flow<String>,
    onLoaded: (String) -> Unit = {},
) {
    var text by remember { mutableStateOf(AnnotatedString("")) }
    val updatedOnLoaded = rememberUpdatedState(onLoaded)

    Text(
        text = text,
        modifier = modifier.animateContentSize(tween(500)),
        inlineContent = mapOf(
            LOADER_INLINE_ID to InlineTextContent(
                Placeholder(
                    width = 1.em,
                    height = 1.em,
                    placeholderVerticalAlign = PlaceholderVerticalAlign.TextCenter,
                ),
            ) {
                val infiniteTransition = rememberInfiniteTransition()

                val scale by infiniteTransition.animateFloat(
                    initialValue = 0.7f,
                    targetValue = 1.0f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(500),
                        repeatMode = RepeatMode.Reverse,
                    ),
                )

                Box(
                    modifier = Modifier
                        .scale(scale)
                        .fillMaxSize()
                        .background(
                            brush = Brush.linearGradient(
                                listOf(
                                    MaterialTheme.colorScheme.onPrimaryContainer,
                                    MaterialTheme.colorScheme.onTertiaryContainer,
                                ),
                            ),
                            shape = CircleShape,
                        ),
                )
            },
        ),
    )

    LaunchedEffect(key1 = stream) {
        // Initialize text with a loader
        text = buildAnnotatedString {
            appendInlineContent(LOADER_INLINE_ID, LOADER_ALT_TEXT)
        }

        // Text will be animated word by word instead of characters. So separate out words out of
        // stream and then append each word on to the display text with a loader appended to it.
        val completeText = stream
            .map { it.split(" ") }
            .fold("") { completeText, words ->
                var newText = completeText
                words.forEachIndexed { index, word ->
                    newText += if (words.lastIndex == index) word else word.plus(" ")

                    // Add a delay to make the animation visible
                    delay(50)

                    // Update the text with a loader appended to it
                    text = buildAnnotatedString {
                        append(newText)
                        appendInlineContent(LOADER_INLINE_ID, LOADER_ALT_TEXT)
                    }
                }
                newText
            }

        // Finally, remove the loader and add plain response text
        text = AnnotatedString(completeText)
        updatedOnLoaded.value.invoke(text.toString())
    }
}
