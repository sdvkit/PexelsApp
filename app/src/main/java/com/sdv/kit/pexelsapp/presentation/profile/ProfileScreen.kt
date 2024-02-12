package com.sdv.kit.pexelsapp.presentation.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import coil.compose.AsyncImage
import com.sdv.kit.pexelsapp.R
import com.sdv.kit.pexelsapp.domain.model.UserDetails
import com.sdv.kit.pexelsapp.presentation.Dimens
import com.sdv.kit.pexelsapp.presentation.anim.bounceClickEffect
import com.sdv.kit.pexelsapp.presentation.annotation.LightAndDarkPreview
import com.sdv.kit.pexelsapp.presentation.profile.section.ProfileDetailsSection
import com.sdv.kit.pexelsapp.presentation.profile.section.ProfileTopBarSection
import com.sdv.kit.pexelsapp.presentation.ui.theme.AppTheme
import com.sdv.kit.pexelsapp.presentation.ui.theme.White

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    userDetails: UserDetails,
    onLogoutClicked: () -> Unit,
    onBackButtonClicked: () -> Unit
) {
    Column(
        modifier = modifier.background(
            color = AppTheme.colors.surface
        ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val placeholder = painterResource(R.drawable.img_placeholder)
        val avatarUrl = userDetails.profilePictureUrl ?: ""
        val username = userDetails.username ?: ""

        Spacer(modifier = Modifier.height(Dimens.PADDING_MEDIUM_SMALLER))
        ProfileTopBarSection(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Dimens.PADDING_MEDIUM),
            onBackButtonClicked = onBackButtonClicked
        )
        Spacer(modifier = Modifier.height(Dimens.PADDING_BIG))
        AsyncImage(
            modifier = Modifier
                .size(Dimens.PROFILE_SCREEN_AVATAR_SIZE)
                .clip(CircleShape),
            model = avatarUrl,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            error = placeholder,
            placeholder = placeholder
        )
        Spacer(modifier = Modifier.height(Dimens.PADDING_MEDIUM))
        Text(
            text = username,
            color = AppTheme.colors.textColor,
            style = AppTheme.typography.displayLarge
        )
        Spacer(modifier = Modifier.height(Dimens.PADDING_MEDIUM))
        ProfileDetailsSection(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Dimens.PADDING_MEDIUM)
                .background(
                    color = AppTheme.colors.surfaceVariant,
                    shape = AppTheme.shapes.medium
                ),
            userDetails = userDetails
        )
        Spacer(modifier = Modifier.height(Dimens.PADDING_MEDIUM))
        LogoutButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Dimens.PADDING_MEDIUM)
                .bounceClickEffect(),
            onClick = onLogoutClicked
        )
    }
}

@Composable
private fun LogoutButton(
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
        Text(
            modifier = Modifier.padding(all = Dimens.PADDING_EXTRA_SMALL),
            text = stringResource(R.string.logout),
            color = White,
            style = AppTheme.typography.labelMedium
        )
    }
}

@LightAndDarkPreview
@Composable
fun ProfileScreenPreview() {
    AppTheme {
        ProfileScreen(
            modifier = Modifier.fillMaxSize(),
            userDetails = UserDetails(
                userId = "id",
                username = "sdvkit",
                profilePictureUrl = "",
                phoneNumber = "+3751234567",
                email = "testmail@gmail.com"
            ),
            onLogoutClicked = { },
            onBackButtonClicked = { }
        )
    }
}