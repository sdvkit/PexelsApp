package com.sdv.kit.pexelsapp.presentation.common.item

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import coil.compose.AsyncImage
import com.sdv.kit.pexelsapp.R
import com.sdv.kit.pexelsapp.domain.model.ChatDetails
import com.sdv.kit.pexelsapp.presentation.Dimens
import com.sdv.kit.pexelsapp.presentation.annotation.LightAndDarkPreview
import com.sdv.kit.pexelsapp.presentation.ui.theme.AppTheme

@Composable
fun ChatListItem(
    modifier: Modifier = Modifier,
    chatDetails: ChatDetails
) {
    val placeholder = painterResource(R.drawable.img_placeholder)

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            modifier = Modifier
                .clip(CircleShape)
                .border(
                    width = Dimens.CHAT_IMAGE_BORDER_WIDTH,
                    shape = CircleShape,
                    color = AppTheme.colors.primary
                )
                .size(Dimens.CHAT_IMAGE_SIZE),
            model = chatDetails.chatImageUrl,
            contentDescription = chatDetails.chatName,
            contentScale = ContentScale.Crop,
            error = placeholder,
            placeholder = placeholder
        )
        Spacer(modifier = Modifier.width(Dimens.PADDING_SMALL))
        Column(verticalArrangement = Arrangement.Center) {
            Text(
                text = chatDetails.chatName,
                style = AppTheme.typography.titleMedium,
                color = AppTheme.colors.textColor,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
            Text(
                text = chatDetails.lastMessage,
                style = AppTheme.typography.displaySmall,
                color = AppTheme.colors.secondaryTextColor,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
        }
    }
}

@LightAndDarkPreview
@Composable
fun ChatListItemPreview() {
    AppTheme {
        ChatListItem(
            modifier = Modifier.fillMaxWidth(),
            chatDetails = ChatDetails(
                chatName = "Ivan",
                lastMessage = "Hello world"
            )
        )
    }
}