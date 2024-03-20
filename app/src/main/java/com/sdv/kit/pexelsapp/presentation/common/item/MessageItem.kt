package com.sdv.kit.pexelsapp.presentation.common.item

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.sdv.kit.pexelsapp.R
import com.sdv.kit.pexelsapp.domain.model.Message
import com.sdv.kit.pexelsapp.domain.model.MessageType
import com.sdv.kit.pexelsapp.domain.model.UserDetails
import com.sdv.kit.pexelsapp.presentation.Dimens
import com.sdv.kit.pexelsapp.presentation.annotation.LightAndDarkPreview
import com.sdv.kit.pexelsapp.presentation.ui.theme.AppTheme
import com.sdv.kit.pexelsapp.util.asBitmap

@Composable
fun MessageItem(
    modifier: Modifier = Modifier,
    message: Message,
    user: UserDetails
) {
    when (message.from?.userId == user.userId) {
        true -> when (message.messageType) {
            MessageType.TEXT -> {
                UserSentTextMessageItem(
                    modifier = modifier,
                    message = message
                )
            }

            MessageType.LOCAL_PHOTO, MessageType.REMOTE_PHOTO -> {
                UserSentPhotoMessageItem(
                    modifier = modifier,
                    message = message
                )
            }
        }

        false -> when (message.messageType) {
            MessageType.TEXT -> {
                ReceivedTextMessageItem(
                    modifier = modifier,
                    message = message
                )
            }

            MessageType.LOCAL_PHOTO, MessageType.REMOTE_PHOTO -> {
                ReceivedPhotoMessageItem(
                    modifier = modifier,
                    message = message
                )
            }
        }
    }
}

@Composable
private fun ReceivedPhotoMessageItem(
    modifier: Modifier = Modifier,
    message: Message
) {
    val placeholder = painterResource(R.drawable.img_placeholder)
    var isExpanded by remember { mutableStateOf(false) }

    Row(modifier = modifier) {
        AsyncImage(
            modifier = Modifier
                .clip(CircleShape)
                .border(
                    width = Dimens.CHAT_IMAGE_BORDER_WIDTH,
                    shape = CircleShape,
                    color = AppTheme.colors.primary
                )
                .size(Dimens.CONVERSATION_IMAGE_SIZE),
            model = message.from?.profilePictureUrl,
            contentDescription = message.text,
            contentScale = ContentScale.Crop,
            error = placeholder,
            placeholder = placeholder
        )
        Spacer(modifier = Modifier.width(Dimens.PADDING_EXTRA_SMALL))
        Column(
            modifier = Modifier.clickable { isExpanded = !isExpanded }
        ) {
            Text(
                text = message.from?.username ?: "",
                style = AppTheme.typography.titleSmall,
                color = AppTheme.colors.textColor,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
            Spacer(modifier = Modifier.height(2.dp))
            when (message.messageType) {
                MessageType.LOCAL_PHOTO -> {
                    Image(
                        modifier = Modifier
                            .fillMaxWidth(Dimens.SENT_PHOTO_WIDTH)
                            .clip(AppTheme.shapes.medium),
                        bitmap = message.text.asBitmap().asImageBitmap(),
                        contentDescription = null
                    )
                }

                MessageType.REMOTE_PHOTO -> {
                    AsyncImage(
                        modifier = Modifier
                            .fillMaxWidth(Dimens.SENT_PHOTO_WIDTH)
                            .clip(AppTheme.shapes.medium),
                        model = message.text,
                        contentDescription = null,
                        contentScale = ContentScale.FillWidth,
                        error = placeholder,
                        placeholder = placeholder
                    )
                }

                else -> throw IllegalArgumentException("There's no such MessageType as ${message.messageType}")
            }
        }
    }
}

@Composable
private fun UserSentPhotoMessageItem(
    modifier: Modifier = Modifier,
    message: Message
) {
    val placeholder = painterResource(R.drawable.img_placeholder)

    Row(
        modifier = modifier
    ) {
        Box(
            modifier = Modifier.weight(1f),
            contentAlignment = Alignment.CenterEnd
        ) {
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = message.from?.username ?: "",
                    style = AppTheme.typography.titleSmall,
                    color = AppTheme.colors.textColor,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )
                Spacer(modifier = Modifier.height(2.dp))
                when (message.messageType) {
                    MessageType.LOCAL_PHOTO -> {
                        Image(
                            modifier = Modifier
                                .fillMaxWidth(Dimens.SENT_PHOTO_WIDTH)
                                .clip(AppTheme.shapes.medium),
                            bitmap = message.text.asBitmap().asImageBitmap(),
                            contentDescription = null
                        )
                    }

                    MessageType.REMOTE_PHOTO -> {
                        AsyncImage(
                            modifier = Modifier
                                .fillMaxWidth(Dimens.SENT_PHOTO_WIDTH)
                                .clip(AppTheme.shapes.medium),
                            model = message.text,
                            contentDescription = null,
                            contentScale = ContentScale.FillWidth,
                            error = placeholder,
                            placeholder = placeholder
                        )
                    }

                    else -> throw IllegalArgumentException("There's no such MessageType as ${message.messageType}")
                }
            }
        }
        Spacer(modifier = Modifier.width(Dimens.PADDING_EXTRA_SMALL))
        AsyncImage(
            modifier = Modifier
                .clip(CircleShape)
                .border(
                    width = Dimens.CHAT_IMAGE_BORDER_WIDTH,
                    shape = CircleShape,
                    color = AppTheme.colors.primary
                )
                .size(Dimens.CONVERSATION_IMAGE_SIZE),
            model = message.from?.profilePictureUrl,
            contentDescription = message.text,
            contentScale = ContentScale.Crop,
            error = placeholder,
            placeholder = placeholder
        )
    }
}


@Composable
private fun UserSentTextMessageItem(
    modifier: Modifier = Modifier,
    message: Message
) {
    val placeholder = painterResource(R.drawable.img_placeholder)
    var isExpanded by remember { mutableStateOf(false) }

    Row(
        modifier = modifier
    ) {
        Box(
            modifier = Modifier.weight(1f),
            contentAlignment = Alignment.CenterEnd
        ) {
            Column(
                modifier = Modifier.clickable { isExpanded = !isExpanded },
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = message.from?.username ?: "",
                    style = AppTheme.typography.titleSmall,
                    color = AppTheme.colors.textColor,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )
                Spacer(modifier = Modifier.height(2.dp))
                Surface(
                    modifier = Modifier.animateContentSize(),
                    shape = AppTheme.shapes.small,
                    shadowElevation = 1.dp,
                    color = AppTheme.colors.surfaceVariant
                ) {
                    Text(
                        modifier = Modifier.padding(all = 6.dp),
                        text = message.text,
                        style = AppTheme.typography.bodyMedium,
                        color = AppTheme.colors.textColor,
                        maxLines = if (isExpanded) Int.MAX_VALUE else 1,
                        overflow = if (isExpanded) TextOverflow.Visible else TextOverflow.Ellipsis
                    )
                }
            }
        }
        Spacer(modifier = Modifier.width(Dimens.PADDING_EXTRA_SMALL))
        AsyncImage(
            modifier = Modifier
                .clip(CircleShape)
                .border(
                    width = Dimens.CHAT_IMAGE_BORDER_WIDTH,
                    shape = CircleShape,
                    color = AppTheme.colors.primary
                )
                .size(Dimens.CONVERSATION_IMAGE_SIZE),
            model = message.from?.profilePictureUrl,
            contentDescription = message.text,
            contentScale = ContentScale.Crop,
            error = placeholder,
            placeholder = placeholder
        )
    }
}

@Composable
private fun ReceivedTextMessageItem(
    modifier: Modifier = Modifier,
    message: Message
) {
    val placeholder = painterResource(R.drawable.img_placeholder)
    var isExpanded by remember { mutableStateOf(false) }

    Row(modifier = modifier) {
        AsyncImage(
            modifier = Modifier
                .clip(CircleShape)
                .border(
                    width = Dimens.CHAT_IMAGE_BORDER_WIDTH,
                    shape = CircleShape,
                    color = AppTheme.colors.primary
                )
                .size(Dimens.CONVERSATION_IMAGE_SIZE),
            model = message.from?.profilePictureUrl,
            contentDescription = message.text,
            contentScale = ContentScale.Crop,
            error = placeholder,
            placeholder = placeholder
        )
        Spacer(modifier = Modifier.width(Dimens.PADDING_EXTRA_SMALL))
        Column(
            modifier = Modifier.clickable { isExpanded = !isExpanded }
        ) {
            Text(
                text = message.from?.username ?: "",
                style = AppTheme.typography.titleSmall,
                color = AppTheme.colors.textColor,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
            Spacer(modifier = Modifier.height(2.dp))
            Surface(
                modifier = Modifier.animateContentSize(),
                shape = AppTheme.shapes.small,
                shadowElevation = 1.dp,
                color = AppTheme.colors.surfaceVariant
            ) {
                Text(
                    modifier = Modifier.padding(all = 6.dp),
                    text = message.text,
                    style = AppTheme.typography.bodyMedium,
                    color = AppTheme.colors.textColor,
                    maxLines = if (isExpanded) Int.MAX_VALUE else 1,
                    overflow = if (isExpanded) TextOverflow.Visible else TextOverflow.Ellipsis
                )
            }
        }
    }
}

@LightAndDarkPreview
@Composable
fun MessageItemPreview() {
    AppTheme {
        val sender = UserDetails(
            username = "Mikola Delopata",
            userId = "123"
        )

        MessageItem(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = AppTheme.colors.surface),
            message = Message(
                from = sender,
                text = "Hello World"
            ),
            user = sender
        )
    }
}