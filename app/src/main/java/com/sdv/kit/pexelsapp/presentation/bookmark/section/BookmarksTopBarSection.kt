package com.sdv.kit.pexelsapp.presentation.bookmark.section

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.sdv.kit.pexelsapp.R
import com.sdv.kit.pexelsapp.presentation.Dimens
import com.sdv.kit.pexelsapp.presentation.annotation.LightAndDarkPreview
import com.sdv.kit.pexelsapp.presentation.ui.theme.AppTheme

@Composable
fun BookmarksTopBarSection(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(R.string.bookmarks),
            style = AppTheme.typography.titleMedium,
            color = AppTheme.colors.textColorVariant,
            textAlign = TextAlign.Center
        )
    }
}

@LightAndDarkPreview
@Composable
fun BookmarksTopBarSectionPreview() {
    AppTheme {
        Surface(
            modifier = Modifier.background(
                color = AppTheme.colors.surface
            )
        ) {
            BookmarksTopBarSection(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(all = Dimens.PADDING_MEDIUM)
            )
        }
    }
}