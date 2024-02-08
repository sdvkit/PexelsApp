package com.sdv.kit.pexelsapp.presentation.details

import android.widget.Toast
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import com.sdv.kit.pexelsapp.domain.model.Photo
import com.sdv.kit.pexelsapp.domain.model.PhotoSrc
import com.sdv.kit.pexelsapp.presentation.Dimens
import com.sdv.kit.pexelsapp.presentation.annotation.LightAndDarkPreview
import com.sdv.kit.pexelsapp.presentation.common.card.PhotoCard
import com.sdv.kit.pexelsapp.presentation.common.stub.ImageNotFoundStub
import com.sdv.kit.pexelsapp.presentation.details.section.DetailsActionsSection
import com.sdv.kit.pexelsapp.presentation.details.section.DetailsTopBarSection
import com.sdv.kit.pexelsapp.presentation.networkConnectionStatus
import com.sdv.kit.pexelsapp.presentation.ui.theme.AppTheme

@Composable
fun DetailsScreen(
    modifier: Modifier = Modifier,
    photo: Photo?,
    photoError: Throwable?,
    isPhotoLoading: Boolean,
    onExplorePhotos: () -> Unit,
    onBackButtonClicked: () -> Unit,
    onDownloadButtonClicked: () -> Unit,
    onBookmarkButtonClicked: () -> Unit,
    isBookmarkButtonActive: Boolean
) {
    val context = LocalContext.current
    val isInternetConnection = networkConnectionStatus()

    val isPhotoImageLoading = remember { mutableStateOf(true) }
    var shouldShowImageNotFoundStub by remember { mutableStateOf(false) }
    val authorName = photo?.photographer ?: ""

    isPhotoImageLoading.value = isPhotoLoading
    shouldShowImageNotFoundStub =
        (!isPhotoImageLoading.value || !isInternetConnection.value || photoError != null) && photo == null

    LaunchedEffect(photoError) {
        if (photoError != null) {
            Toast.makeText(context, photoError.message, Toast.LENGTH_LONG).show()
        }
    }

    Column(
        modifier = modifier
            .background(
                color = AppTheme.colors.surface
            )
            .verticalScroll(rememberScrollState())
            .padding(horizontal = Dimens.PADDING_MEDIUM),
    ) {
        Spacer(modifier = Modifier.height(Dimens.PADDING_MEDIUM_SMALLER))
        DetailsTopBarSection(
            modifier = Modifier.fillMaxWidth(),
            onBackButtonClicked = onBackButtonClicked,
            authorName = authorName
        )

        if (isPhotoImageLoading.value && !shouldShowImageNotFoundStub && photoError == null) {
            Spacer(modifier = Modifier.height(Dimens.PADDING_SMALL))
            LinearProgressIndicator(
                modifier = Modifier
                    .padding(end = Dimens.PADDING_SMALL)
                    .clip(CircleShape)
                    .fillMaxWidth()
                    .height(Dimens.LOADING_PROGRESS_BAR_HEIGHT),
                color = AppTheme.colors.progressBackground,
                trackColor = AppTheme.colors.primary
            )
        }

        when {
            shouldShowImageNotFoundStub -> {
                Spacer(modifier = Modifier.weight(1f))
                ImageNotFoundStub(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    onClick = onExplorePhotos
                )
                Spacer(modifier = Modifier.weight(1f))
            }

            photo != null -> {
                Spacer(modifier = Modifier.height(Dimens.PADDING_MEDIUM_BIGGER))
                BoxWithConstraints {
                    var isPinchEnabled by remember { mutableStateOf(true) }
                    var scale by remember { mutableFloatStateOf(1f) }
                    val animatedScale by animateFloatAsState(targetValue = scale, label = "Scale")
                    var offset by remember { mutableStateOf(Offset.Zero) }

                    val state = rememberTransformableState { zoomChange, panChange, _ ->
                        scale = (scale * zoomChange).coerceIn(1f, 5f)

                        val extraWidth = (scale - 1) * constraints.maxWidth
                        val extraHeight = (scale - 1) * constraints.maxHeight

                        val maxX = extraWidth / 2
                        val maxY = extraHeight / 2

                        offset = Offset(
                            x = (offset.x + scale * panChange.x).coerceIn(-maxX, maxX),
                            y = (offset.y + scale * panChange.y).coerceIn(-maxY, maxY),
                        )

                        if (zoomChange == 1f && scale == 1f && offset == Offset.Zero) {
                            isPinchEnabled = false
                        }
                    }

                    DisposableEffect(state.isTransformInProgress) {
                        onDispose {
                            scale = 1f
                            offset = Offset.Zero
                        }
                    }

                    PhotoCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(AppTheme.shapes.medium)
                            .graphicsLayer {
                                scaleX = animatedScale
                                scaleY = animatedScale
                                translationX = offset.x
                            }
                            .transformable(state = state, enabled = isPinchEnabled)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = rememberRipple(color = Color.Transparent)
                            ) {
                                isPinchEnabled = true
                            },
                        contentScale = ContentScale.Fit,
                        photo = photo,
                        isPhotoLoading = isPhotoImageLoading
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                Spacer(modifier = Modifier.height(Dimens.PADDING_MEDIUM))
                DetailsActionsSection(
                    modifier = Modifier.fillMaxWidth(),
                    onDownloadButtonClicked = onDownloadButtonClicked,
                    onBookmarkButtonClicked = onBookmarkButtonClicked,
                    isBookmarkButtonActive = isBookmarkButtonActive
                )
                Spacer(modifier = Modifier.height(Dimens.PADDING_MEDIUM))
            }
        }
    }
}

@LightAndDarkPreview
@Composable
fun DetailsScreenPreview() {
    AppTheme {
        DetailsScreen(
            modifier = Modifier.fillMaxSize(),
            isPhotoLoading = false,
            onBackButtonClicked = { },
            onDownloadButtonClicked = { },
            onBookmarkButtonClicked = { },
            isBookmarkButtonActive = false,
            onExplorePhotos = {},
            photo = Photo(
                photoId = 1,
                width = 1920,
                height = 1080,
                photographer = "Nikita Sudaev",
                src = PhotoSrc(original = "https://images.pexels.com/photos/19797263/pexels-photo-19797263.jpeg"),
                alt = "Something"
            ),
            photoError = null
        )
    }
}