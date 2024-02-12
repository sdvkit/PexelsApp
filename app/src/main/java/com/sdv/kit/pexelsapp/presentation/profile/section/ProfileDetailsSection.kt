package com.sdv.kit.pexelsapp.presentation.profile.section

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.sdv.kit.pexelsapp.R
import com.sdv.kit.pexelsapp.domain.model.UserDetails
import com.sdv.kit.pexelsapp.presentation.Dimens
import com.sdv.kit.pexelsapp.presentation.annotation.LightAndDarkPreview
import com.sdv.kit.pexelsapp.presentation.ui.theme.AppTheme

@Composable
fun ProfileDetailsSection(
    modifier: Modifier = Modifier,
    userDetails: UserDetails
) {
    val email = userDetails.email
    val phoneNumber = userDetails.phoneNumber ?: ""

    Column(
        modifier = modifier
    ) {
        ProfileDetailsItem(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = Dimens.PADDING_MEDIUM_SMALLER),
            icon = R.drawable.ic_mail,
            text = email
        )
        ProfileDetailsItem(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = Dimens.PADDING_MEDIUM_SMALLER),
            icon = R.drawable.ic_phone,
            text = phoneNumber.ifBlank {
                stringResource(R.string.none)
            }
        )
    }
}

@Composable
private fun ProfileDetailsItem(
    modifier: Modifier = Modifier,
    @DrawableRes icon: Int,
    text: String
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(modifier = Modifier.width(Dimens.PADDING_SMALL))
        Icon(
            painter = painterResource(icon),
            contentDescription = null,
            tint = AppTheme.colors.primary
        )
        Spacer(modifier = Modifier.width(Dimens.PADDING_MEDIUM_SMALLER))
        Text(
            text = text,
            color = AppTheme.colors.textColor,
            style = AppTheme.typography.bodyMedium
        )
        Spacer(modifier = Modifier.width(Dimens.PADDING_SMALL))
    }
}

@LightAndDarkPreview
@Composable
fun ProfileDetailsSectionPreview() {
    AppTheme {
        ProfileDetailsSection(
            modifier = Modifier.fillMaxWidth(),
            userDetails = UserDetails(
                userId = "id",
                username = "sdvkit",
                profilePictureUrl = "",
                phoneNumber = "+3751234567",
                email = "testmail@gmail.com"
            )
        )
    }
}