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
    onLoaded: (String) -> Unit = {}
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
                    placeholderVerticalAlign = PlaceholderVerticalAlign.TextCenter
                )
            ) {
                val infiniteTransition = rememberInfiniteTransition()

                val scale by infiniteTransition.animateFloat(
                    initialValue = 0.7f,
                    targetValue = 1.0f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(500),
                        repeatMode = RepeatMode.Reverse
                    )
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
                                )
                            ),
                            shape = CircleShape
                        )
                )
            }
        )
    )

    LaunchedEffect(key1 = stream) {
        fun replaceLoaderWithBlank(): String {
            return text
                .replaceRange(text.length - LOADER_ALT_TEXT.length, text.length, "")
                .toString()
        }

        // Initialize text with a loader
        text = buildAnnotatedString {
            appendInlineContent(LOADER_INLINE_ID, LOADER_ALT_TEXT)
        }

        // Text will be animated word by word instead of characters. So separate out words out of
        // stream and then append each word on to the display text with a loader appended to it.
        stream.map { it.split(" ") }.collect {
            it.forEach { word ->
                delay(50)
                text = buildAnnotatedString {
                    append(replaceLoaderWithBlank())
                    append("$word ")
                    appendInlineContent(LOADER_INLINE_ID, LOADER_ALT_TEXT)
                }
            }
        }

        // Finally, remove the loader
        text = buildAnnotatedString {
            append(replaceLoaderWithBlank())
        }
        updatedOnLoaded.value.invoke(text.toString())
    }
}