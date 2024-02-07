package com.sdv.kit.pexelsapp.presentation.details.section

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.sdv.kit.pexelsapp.presentation.Dimens
import com.sdv.kit.pexelsapp.presentation.anim.bounceClickEffect
import com.sdv.kit.pexelsapp.presentation.annotation.LightAndDarkPreview
import com.sdv.kit.pexelsapp.presentation.common.button.BackButton
import com.sdv.kit.pexelsapp.presentation.ui.theme.AppTheme

@Composable
fun DetailsTopBarSection(
    modifier: Modifier = Modifier,
    onBackButtonClicked: () -> Unit,
    authorName: String
) {
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
        Text(
            text = authorName,
            style = AppTheme.typography.titleMedium,
            color = AppTheme.colors.textColorVariant
        )
        Spacer(modifier = Modifier.weight(1f))
        Spacer(modifier = Modifier.width(Dimens.BACK_BUTTON_SIZE))
    }
}

@LightAndDarkPreview
@Composable
fun DetailsTopBarSectionPreview() {
    AppTheme {
        Surface(
            modifier = Modifier.background(
                color = AppTheme.colors.surface
            )
        ) {
            DetailsTopBarSection(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(all = Dimens.PADDING_MEDIUM),
                onBackButtonClicked = { },
                authorName = "Name Surname"
            )
        }
    }
}