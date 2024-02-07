package com.sdv.kit.pexelsapp.presentation.details.section

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.sdv.kit.pexelsapp.presentation.Dimens
import com.sdv.kit.pexelsapp.presentation.anim.bounceClickEffect
import com.sdv.kit.pexelsapp.presentation.annotation.LightAndDarkPreview
import com.sdv.kit.pexelsapp.presentation.common.button.BookmarkButton
import com.sdv.kit.pexelsapp.presentation.common.button.DownloadButton
import com.sdv.kit.pexelsapp.presentation.ui.theme.AppTheme

@Composable
fun DetailsActionsSection(
    modifier: Modifier = Modifier,
    onDownloadButtonClicked: () -> Unit,
    onBookmarkButtonClicked: () -> Unit,
    isBookmarkButtonActive: Boolean
) {
    Row(modifier = modifier) {
        DownloadButton(
            modifier = Modifier
                .width(Dimens.DOWNLOAD_BUTTON_WIDTH)
                .height(Dimens.DOWNLOAD_BUTTON_HEIGHT)
                .bounceClickEffect(0.9f),
            onClick = onDownloadButtonClicked
        )
        Spacer(modifier = Modifier.weight(1f))
        BookmarkButton(
            modifier = Modifier
                .size(Dimens.BOOKMARK_BUTTON_SIZE)
                .bounceClickEffect(),
            onClick = onBookmarkButtonClicked,
            isActive = isBookmarkButtonActive
        )
    }
}

@LightAndDarkPreview
@Composable
fun DetailsActionsSectionPreview() {
    AppTheme {
        Surface(
            modifier = Modifier
                .background(color = AppTheme.colors.surface)
        ) {
            DetailsActionsSection(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(all = Dimens.PADDING_MEDIUM),
                onDownloadButtonClicked = { },
                onBookmarkButtonClicked = { },
                isBookmarkButtonActive = false
            )
        }
    }
}