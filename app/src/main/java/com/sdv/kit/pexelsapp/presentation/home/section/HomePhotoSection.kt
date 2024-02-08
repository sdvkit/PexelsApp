package com.sdv.kit.pexelsapp.presentation.home.section

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.sdv.kit.pexelsapp.domain.model.Photo
import com.sdv.kit.pexelsapp.presentation.Dimens
import com.sdv.kit.pexelsapp.presentation.anim.bounceClickEffect
import com.sdv.kit.pexelsapp.presentation.annotation.LightAndDarkPreview
import com.sdv.kit.pexelsapp.presentation.common.card.PhotoCard
import com.sdv.kit.pexelsapp.presentation.ui.theme.AppTheme
import kotlinx.coroutines.flow.MutableStateFlow

@Composable
fun HomePhotoSection(
    modifier: Modifier = Modifier,
    photos: LazyPagingItems<Photo>,
    onAnyPhotoClicked: (Photo) -> Unit
) {
    LazyVerticalStaggeredGrid(
        modifier = modifier,
        columns = StaggeredGridCells.Fixed(2),
        verticalItemSpacing = Dimens.PADDING_MEDIUM_SMALLER,
        horizontalArrangement = Arrangement.spacedBy(Dimens.PADDING_MEDIUM_SMALLER),
    ) {
        items(count = photos.itemCount) { index ->
            val photo = photos[index]
            val isPhotoLoading = remember {
                mutableStateOf(true)
            }

            if (photo != null) {
                PhotoCard(
                    modifier = Modifier
                        .bounceClickEffect(valueTo = 0.9f)
                        .fillMaxWidth()
                        .height(photo.height.dp / Dimens.PHOTO_CARD_HEIGHT_DELIMITER)
                        .clip(AppTheme.shapes.large)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = rememberRipple(color = Color.Transparent)
                        ) {
                            onAnyPhotoClicked(photo)
                        },
                    photo = photo,
                    isPhotoLoading = isPhotoLoading
                )
            }
        }
    }
}

@LightAndDarkPreview
@Composable
fun HomePhotoSectionPreview() {
    AppTheme {
        Surface(
            modifier = Modifier.background(
                color = AppTheme.colors.surface
            )
        ) {
            val photos = MutableStateFlow<PagingData<Photo>>(
                PagingData.empty()
            ).collectAsLazyPagingItems()

            HomePhotoSection(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(all = Dimens.PADDING_MEDIUM),
                photos = photos,
                onAnyPhotoClicked = { }
            )
        }
    }
}