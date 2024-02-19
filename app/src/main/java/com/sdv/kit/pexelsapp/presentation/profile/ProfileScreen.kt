package com.sdv.kit.pexelsapp.presentation.profile

import android.graphics.Bitmap
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import coil.compose.AsyncImage
import com.sdv.kit.pexelsapp.R
import com.sdv.kit.pexelsapp.domain.model.UserDetails
import com.sdv.kit.pexelsapp.presentation.Dimens
import com.sdv.kit.pexelsapp.presentation.annotation.LightAndDarkPreview
import com.sdv.kit.pexelsapp.presentation.profile.section.LocalPhotosSection
import com.sdv.kit.pexelsapp.presentation.profile.section.ProfileDetailsSection
import com.sdv.kit.pexelsapp.presentation.profile.section.ProfileTopBarSection
import com.sdv.kit.pexelsapp.presentation.ui.theme.AppTheme

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    userDetails: UserDetails,
    onLogoutClicked: () -> Unit,
    onBackButtonClicked: () -> Unit,
    onTakePhotoClicked: () -> Unit,
    isStoragePermissionGranted: Boolean,
    onPermissionRequest: () -> Unit,
    images: List<Bitmap>
) {
    val placeholder = painterResource(R.drawable.img_placeholder)
    val avatarUrl = userDetails.profilePictureUrl ?: ""
    val username = userDetails.username ?: ""

    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .background(
                color = AppTheme.colors.surface
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(Dimens.PADDING_MEDIUM_SMALLER))
        ProfileTopBarSection(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Dimens.PADDING_MEDIUM),
            onBackButtonClicked = onBackButtonClicked,
            onLogoutButtonClicked = onLogoutClicked
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
        LocalPhotosSection(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Dimens.PADDING_MEDIUM),
            onTakePhotoButtonClicked = onTakePhotoClicked,
            isStoragePermissionGranted = isStoragePermissionGranted,
            onPermissionRequest = onPermissionRequest,
            images = images
        )
    }
}

@LightAndDarkPreview
@Composable
fun ProfileScreenPreview() {
    AppTheme {
        ProfileScreen(
            modifier = Modifier.fillMaxSize(),
            isStoragePermissionGranted = false,
            onPermissionRequest = { },
            userDetails = UserDetails(
                userId = "id",
                username = "sdvkit",
                profilePictureUrl = "",
                phoneNumber = "+3751234567",
                email = "testmail@gmail.com"
            ),
            onLogoutClicked = { },
            onBackButtonClicked = { },
            onTakePhotoClicked = { },
            images = listOf()
        )
    }
}