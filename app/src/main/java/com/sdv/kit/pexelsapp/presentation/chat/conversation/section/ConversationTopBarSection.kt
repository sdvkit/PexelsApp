package com.sdv.kit.pexelsapp.presentation.chat.conversation.section

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import coil.compose.AsyncImage
import com.sdv.kit.pexelsapp.R
import com.sdv.kit.pexelsapp.presentation.Dimens
import com.sdv.kit.pexelsapp.presentation.anim.bounceClickEffect
import com.sdv.kit.pexelsapp.presentation.annotation.LightAndDarkPreview
import com.sdv.kit.pexelsapp.presentation.common.button.BackButton
import com.sdv.kit.pexelsapp.presentation.common.button.DetailsButton
import com.sdv.kit.pexelsapp.presentation.ui.theme.AppTheme

@Composable
fun ConversationTopBarSection(
    modifier: Modifier = Modifier,
    imageUrl: String,
    header: String,
    onBackButtonClicked: () -> Unit,
    onDetailsButtonClicked: () -> Unit
) {
    val placeholder = painterResource(R.drawable.img_placeholder)

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        BackButton(
            modifier = Modifier
                .size(Dimens.BACK_BUTTON_SIZE)
                .bounceClickEffect(),
            onClick = onBackButtonClicked
        )
        Spacer(modifier = Modifier.weight(1f))
        Row(
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
                    .size(Dimens.CONVERSATION_AVATAR_SIZE),
                model = imageUrl,
                contentDescription = header,
                contentScale = ContentScale.Crop,
                error = placeholder,
                placeholder = placeholder
            )
            Spacer(modifier = Modifier.width(Dimens.PADDING_SMALL))
            Text(
                text = header,
                style = AppTheme.typography.titleMedium,
                color = AppTheme.colors.textColorVariant
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        DetailsButton(
            modifier = Modifier
                .size(Dimens.DETAILS_BUTTON_SIZE)
                .bounceClickEffect(),
            onClick = onDetailsButtonClicked
        )
    }
}

@LightAndDarkPreview
@Composable
fun ConversationTopBarSectionPreview() {
    AppTheme {
        Surface(
            modifier = Modifier.background(
                color = AppTheme.colors.surface
            )
        ) {
            ConversationTopBarSection(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(all = Dimens.PADDING_MEDIUM),
                header = "Ivan",
                imageUrl = "",
                onBackButtonClicked = { },
                onDetailsButtonClicked = { }
            )
        }
    }
}