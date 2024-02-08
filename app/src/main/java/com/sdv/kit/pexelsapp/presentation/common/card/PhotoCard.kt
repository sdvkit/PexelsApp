package com.sdv.kit.pexelsapp.presentation.common.card

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import coil.compose.AsyncImage
import com.sdv.kit.pexelsapp.R
import com.sdv.kit.pexelsapp.domain.model.Photo
import com.sdv.kit.pexelsapp.domain.model.PhotoSrc
import com.sdv.kit.pexelsapp.presentation.Dimens
import com.sdv.kit.pexelsapp.presentation.annotation.LightAndDarkPreview
import com.sdv.kit.pexelsapp.presentation.ui.theme.AppTheme

@Composable
fun PhotoCard(
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop,
    photo: Photo,
    isPhotoLoading: MutableState<Boolean>
) {
    val placeholder = painterResource(R.drawable.img_placeholder)
    val imageUrl = photo.src.original

    AsyncImage(
        modifier = modifier,
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

    if (isPhotoLoading.value) {
        PhotoCardShimmer(modifier = modifier)
    }
}

@LightAndDarkPreview
@Composable
fun PhotoCardPreview() {
    AppTheme {
        Surface {
            PhotoCard(
                modifier = Modifier.padding(all = Dimens.PADDING_MEDIUM),
                photo = Photo(
                    photoId = 1,
                    width = 1920,
                    height = 1080,
                    photographer = "Nick",
                    src = PhotoSrc(original = "https://images.pexels.com/photos/19797263/pexels-photo-19797263.jpeg"),
                    alt = "Something"
                ),
                isPhotoLoading = remember {
                    mutableStateOf(false)
                }
            )
        }
    }
}

@LightAndDarkPreview
@Composable
fun PhotoCardLoadingPreview() {
    AppTheme {
        Surface {
            PhotoCard(
                modifier = Modifier.padding(all = Dimens.PADDING_MEDIUM),
                photo = Photo(
                    photoId = 1,
                    width = 1920,
                    height = 1080,
                    photographer = "Nick",
                    src = PhotoSrc(original = "https://images.pexels.com/photos/19797263/pexels-photo-19797263.jpeg"),
                    alt = "Something"
                ),
                isPhotoLoading = remember {
                    mutableStateOf(true)
                }
            )
        }
    }
}