package com.sdv.kit.pexelsapp.presentation.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
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
import com.sdv.kit.pexelsapp.presentation.ui.theme.AppTheme
import com.sdv.kit.pexelsapp.presentation.ui.theme.White

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    onGoogleButtonClicked: () -> Unit
) {
    Column(
        modifier = modifier.background(
            color = AppTheme.colors.surface
        ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.weight(1f))
        Icon(
            modifier = Modifier.size(Dimens.LOGIN_SCREEN_LOGO_ICON_SIZE),
            painter = painterResource(R.drawable.ic_logo),
            contentDescription = null,
            tint = AppTheme.colors.primary
        )
        Spacer(modifier = Modifier.height(Dimens.PADDING_BIG))
        BodySection()
        Spacer(modifier = Modifier.weight(0.5f))
        GoogleAuthButton(
            modifier = Modifier
                .widthIn(max = Dimens.LOGIN_SCREEN_BUTTON_MAX_WIDTH)
                .fillMaxWidth()
                .padding(horizontal = Dimens.PADDING_MEDIUM)
                .bounceClickEffect(),
            onClick = onGoogleButtonClicked
        )
        Spacer(modifier = Modifier.weight(1f))
    }
}

@Composable
private fun BodySection(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.login),
            color = AppTheme.colors.textColor,
            style = AppTheme.typography.displayLarge
        )
        Spacer(modifier = Modifier.height(Dimens.PADDING_MEDIUM))
        Text(
            text = stringResource(R.string.login_description),
            color = AppTheme.colors.secondaryTextColor,
            style = AppTheme.typography.bodyLarge
        )
    }
}

@Composable
private fun GoogleAuthButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Button(
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(
            containerColor = AppTheme.colors.primary
        ),
        onClick = onClick
    ) {
        Icon(
            modifier = Modifier.size(Dimens.LOGIN_SCREEN_GOOGLE_ICON_SIZE),
            painter = painterResource(R.drawable.ic_google),
            contentDescription = null,
            tint = White
        )
        Text(
            modifier = Modifier.padding(all = Dimens.PADDING_SMALL),
            text = stringResource(R.string.google_sign_in),
            color = White,
            style = AppTheme.typography.labelMedium
        )
    }
}

@LightAndDarkPreview
@Composable
fun LoginScreenPreview() {
    AppTheme {
        LoginScreen(
            modifier = Modifier.fillMaxSize(),
            onGoogleButtonClicked = { }
        )
    }
}