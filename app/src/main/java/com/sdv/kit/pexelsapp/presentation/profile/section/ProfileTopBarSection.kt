package com.sdv.kit.pexelsapp.presentation.profile.section

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.sdv.kit.pexelsapp.R
import com.sdv.kit.pexelsapp.presentation.Dimens
import com.sdv.kit.pexelsapp.presentation.anim.bounceClickEffect
import com.sdv.kit.pexelsapp.presentation.annotation.LightAndDarkPreview
import com.sdv.kit.pexelsapp.presentation.common.button.BackButton
import com.sdv.kit.pexelsapp.presentation.ui.theme.AppTheme

@Composable
fun ProfileTopBarSection(
    modifier: Modifier = Modifier,
    onBackButtonClicked: () -> Unit,
    onLogoutButtonClicked: () -> Unit
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
            text = stringResource(R.string.profile),
            style = AppTheme.typography.titleMedium,
            color = AppTheme.colors.textColorVariant
        )
        Spacer(modifier = Modifier.weight(1f))
        IconButton(
            modifier = Modifier
                .size(Dimens.LOGOUT_BUTTON_SIZE)
                .bounceClickEffect(),
            onClick = onLogoutButtonClicked
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_logout),
                contentDescription = null,
                tint = AppTheme.colors.primary
            )
        }
    }
}

@LightAndDarkPreview
@Composable
fun ProfileTopBarSectionPreview() {
    AppTheme {
        Surface(
            modifier = Modifier.background(
                color = AppTheme.colors.surface
            )
        ) {
            ProfileTopBarSection(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(all = Dimens.PADDING_MEDIUM),
                onBackButtonClicked = { },
                onLogoutButtonClicked = { }
            )
        }
    }
}