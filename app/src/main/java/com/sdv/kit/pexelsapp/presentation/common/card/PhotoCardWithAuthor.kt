package com.sdv.kit.pexelsapp.presentation.common.card

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import coil.compose.AsyncImage
import com.sdv.kit.pexelsapp.R
import com.sdv.kit.pexelsapp.domain.model.Photo
import com.sdv.kit.pexelsapp.domain.model.PhotoSrc
import com.sdv.kit.pexelsapp.presentation.Dimens
import com.sdv.kit.pexelsapp.presentation.annotation.LightAndDarkPreview
import com.sdv.kit.pexelsapp.presentation.ui.theme.AppTheme

@Composable
fun PhotoCardWithAuthor(
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop,
    photo: Photo,
    isPhotoLoading: MutableState<Boolean>,
    onClick: (Photo) -> Unit
) {
    val placeholder = painterResource(R.drawable.img_placeholder)
    val imageUrl = photo.src.original
    val author = photo.photographer

    Box(
        modifier = modifier,
        contentAlignment = Alignment.BottomCenter
    ) {
        AsyncImage(
            modifier = Modifier.clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(color = Color.Transparent)
            ) {
                onClick(photo)
            },
            onSuccess = {
                isPhotoLoading.value = false
            },
            onError = {
                isPhotoLoading.value = false
            },
            model = imageUrl,
            contentDescription = photo.alt,
            contentScale = contentScale,
            error = placeholder,
            placeholder = placeholder
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = Dimens.PHOTO_AUTHOR_SECTION_MIN_HEIGHT)
                .background(
                    color = Color.Black.copy(
                        alpha = Dimens.PHOTO_AUTHOR_SECTION_ALPHA
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = author,
                style = AppTheme.typography.bodyMedium,
                color = Color.White,
                textAlign = TextAlign.Center
            )
        }
    }

    if (isPhotoLoading.value) {
        PhotoCardShimmer(modifier = modifier)
    }
}

@LightAndDarkPreview
@Composable
fun PhotoCardWithAuthorPreview() {
    AppTheme {
        Surface {
            PhotoCardWithAuthor(
                modifier = Modifier.padding(all = Dimens.PADDING_MEDIUM),
                photo = Photo(
                    photoId = 1,
                    width = 1920,
                    height = 1080,
                    photographer = "Nick",
                    src = PhotoSrc(original = "https://images.pexels.com/photos/19797263/pexels-photo-19797263.jpeg"),
                    alt = "Something"
                ),
                onClick = { },
                isPhotoLoading = remember {
                    mutableStateOf(false)
                }
            )
        }
    }
}

@LightAndDarkPreview
@Composable
fun PhotoCardWithAuthorLoadingPreview() {
    AppTheme {
        Surface {
            PhotoCardWithAuthor(
                modifier = Modifier.padding(all = Dimens.PADDING_MEDIUM),
                photo = Photo(
                    photoId = 1,
                    width = 1920,
                    height = 1080,
                    photographer = "Nick",
                    src = PhotoSrc(original = "https://images.pexels.com/photos/19797263/pexels-photo-19797263.jpeg"),
                    alt = "Something"
                ),
                onClick = { },
                isPhotoLoading = remember {
                    mutableStateOf(true)
                }
            )
        }
    }
}