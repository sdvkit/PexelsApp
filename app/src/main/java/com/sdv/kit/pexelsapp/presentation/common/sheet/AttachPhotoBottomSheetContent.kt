package com.sdv.kit.pexelsapp.presentation.common.sheet

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.sdv.kit.pexelsapp.R
import com.sdv.kit.pexelsapp.domain.model.Bookmarked
import com.sdv.kit.pexelsapp.domain.model.Photo
import com.sdv.kit.pexelsapp.presentation.Dimens
import com.sdv.kit.pexelsapp.presentation.anim.bounceClickEffect
import com.sdv.kit.pexelsapp.presentation.annotation.LightAndDarkPreview
import com.sdv.kit.pexelsapp.presentation.common.TextSpacer
import com.sdv.kit.pexelsapp.presentation.common.card.PhotoCardWithAuthor
import com.sdv.kit.pexelsapp.presentation.common.stub.NotSavedStub
import com.sdv.kit.pexelsapp.presentation.ui.theme.AppTheme
import kotlinx.coroutines.flow.MutableStateFlow

@Composable
fun AttachPhotoBottomSheetContent(
    modifier: Modifier = Modifier,
    onExplore: () -> Unit,
    localImages: List<Bitmap>,
    bookmarkedPhotos: LazyPagingItems<Bookmarked>,
    onAnyLocalImageClicked: (Bitmap) -> Unit,
    onAnyPhotoClicked: (Photo) -> Unit
) {
    Column(modifier = modifier) {
        when (bookmarkedPhotos.itemCount == 0) {
            true -> {
                Spacer(modifier = Modifier.weight(1f))
                NotSavedStub(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    onClick = onExplore
                )
                Spacer(modifier = Modifier.weight(1f))
            }

            false -> {
                LazyVerticalStaggeredGrid(
                    columns = StaggeredGridCells.Fixed(2),
                    verticalItemSpacing = Dimens.PADDING_MEDIUM_SMALLER,
                    horizontalArrangement = Arrangement.spacedBy(Dimens.PADDING_MEDIUM_SMALLER)
                ) {
                    item {
                        Text(
                            text = stringResource(R.string.attach_photo),
                            style = AppTheme.typography.titleMedium,
                            color = AppTheme.colors.textColor
                        )
                    }

                    item {
                        TextSpacer(style = AppTheme.typography.titleMedium)
                    }

                    items(localImages.size) { index ->
                        Image(
                            modifier = Modifier
                                .fillMaxWidth()
                                .bounceClickEffect(valueTo = 0.9f)
                                .clip(AppTheme.shapes.medium)
                                .clickable {
                                    onAnyLocalImageClicked(localImages[index])
                                },
                            bitmap = localImages[index].asImageBitmap(),
                            contentDescription = null
                        )
                    }

                    items(bookmarkedPhotos.itemCount) { index ->
                        val photo = bookmarkedPhotos[index]?.photo
                        val isPhotoLoading = remember { mutableStateOf(true) }

                        if (photo != null) {
                            PhotoCardWithAuthor(
                                modifier = Modifier
                                    .bounceClickEffect(valueTo = 0.9f)
                                    .fillMaxWidth()
                                    .height(photo.height.dp / Dimens.PHOTO_CARD_HEIGHT_DELIMITER)
                                    .clip(AppTheme.shapes.large),
                                photo = photo,
                                onClick = onAnyPhotoClicked,
                                isPhotoLoading = isPhotoLoading
                            )
                        }
                    }
                }
            }
        }
    }
}

@LightAndDarkPreview
@Composable
fun AttachPhotoBottomSheetContentPreview() {
    AppTheme {
        val photos = MutableStateFlow<PagingData<Bookmarked>>(
            PagingData.empty()
        ).collectAsLazyPagingItems()

        AttachPhotoBottomSheetContent(
            modifier = Modifier.fillMaxWidth(),
            localImages = emptyList(),
            bookmarkedPhotos = photos,
            onAnyPhotoClicked = { },
            onAnyLocalImageClicked = { },
            onExplore = { }
        )
    }
}