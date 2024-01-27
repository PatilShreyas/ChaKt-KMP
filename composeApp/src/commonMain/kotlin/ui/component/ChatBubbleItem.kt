package ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import ui.screen.ChatMessage
import ui.screen.ModelChatMessage
import ui.screen.UserChatMessage

@Composable
fun ChatBubbleItem(chatMessage: ChatMessage) {
    val isModelMessage = chatMessage.isModelMessage() || chatMessage.isErrorMessage()

    val backgroundColor = when (chatMessage) {
        is ModelChatMessage.ErrorMessage -> MaterialTheme.colorScheme.errorContainer
        is ModelChatMessage.LoadingModelMessage, is ModelChatMessage.LoadedModelMessage ->
            MaterialTheme.colorScheme.primaryContainer

        is UserChatMessage -> MaterialTheme.colorScheme.tertiaryContainer
    }

    val bubbleShape = if (isModelMessage) {
        RoundedCornerShape(4.dp, 20.dp, 20.dp, 20.dp)
    } else {
        RoundedCornerShape(20.dp, 4.dp, 20.dp, 20.dp)
    }

    val horizontalAlignment = if (isModelMessage) Alignment.Start else Alignment.End

    Column(
        horizontalAlignment = horizontalAlignment,
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .fillMaxWidth()
    ) {
        Text(
            text = if (chatMessage.isModelMessage()) "AI" else "You",
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        Row {
            BoxWithConstraints {
                Card(
                    colors = CardDefaults.cardColors(containerColor = backgroundColor),
                    shape = bubbleShape,
                    modifier = Modifier.widthIn(0.dp, maxWidth * 0.9f)
                ) {
                    when (chatMessage) {
                        is ModelChatMessage.ErrorMessage -> Text(
                            text = chatMessage.text,
                            modifier = Modifier.padding(16.dp)
                        )

                        is ModelChatMessage.LoadingModelMessage -> LoadingText(
                            stream = chatMessage.textStream,
                            modifier = Modifier.padding(16.dp)
                        )

                        is UserChatMessage -> Column {
                            chatMessage.image?.let { attachedImage ->
                                Image(
                                    bitmap = attachedImage,
                                    contentDescription = "Attachment",
                                    modifier = Modifier
                                        .padding(8.dp)
                                        .height(192.dp)
                                        .clip(RoundedCornerShape(16.dp)),
                                    contentScale = ContentScale.FillHeight
                                )
                            }
                            Text(
                                text = chatMessage.text,
                                modifier = Modifier.padding(16.dp)
                            )
                        }

                        is ModelChatMessage.LoadedModelMessage -> Text(
                            text = chatMessage.text,
                            modifier = Modifier.padding(16.dp)
                        )
                    }

                }
            }
        }
    }
}