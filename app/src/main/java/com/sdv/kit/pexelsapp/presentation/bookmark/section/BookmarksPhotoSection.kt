package com.sdv.kit.pexelsapp.presentation.bookmark.section

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.sdv.kit.pexelsapp.domain.model.Bookmarked
import com.sdv.kit.pexelsapp.domain.model.Photo
import com.sdv.kit.pexelsapp.presentation.Dimens
import com.sdv.kit.pexelsapp.presentation.anim.bounceClickEffect
import com.sdv.kit.pexelsapp.presentation.annotation.LightAndDarkPreview
import com.sdv.kit.pexelsapp.presentation.common.card.PhotoCardWithAuthor
import com.sdv.kit.pexelsapp.presentation.ui.theme.AppTheme
import kotlinx.coroutines.flow.MutableStateFlow

@Composable
fun BookmarksPhotoSection(
    modifier: Modifier = Modifier,
    bookmarkedPhotos: LazyPagingItems<Bookmarked>,
    isAnyPhotoLoading: MutableState<Boolean>,
    onAnyPhotoClicked: (Photo) -> Unit
) {
    LazyVerticalStaggeredGrid(
        modifier = modifier,
        columns = StaggeredGridCells.Fixed(2),
        verticalItemSpacing = Dimens.PADDING_MEDIUM_SMALLER,
        horizontalArrangement = Arrangement.spacedBy(Dimens.PADDING_MEDIUM_SMALLER)
    ) {
        items(bookmarkedPhotos.itemCount) { index ->
            val photo = bookmarkedPhotos[index]?.photo
            val isPhotoLoading = remember { mutableStateOf(true) }
            isAnyPhotoLoading.value = isPhotoLoading.value

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

@LightAndDarkPreview
@Composable
fun BookmarksPhotoSectionPreview() {
    AppTheme {
        val isPhotoLoading = remember {
            mutableStateOf(false)
        }

        Surface(
            modifier = Modifier.background(
                color = AppTheme.colors.surface
            )
        ) {
            val photos = MutableStateFlow<PagingData<Bookmarked>>(
                PagingData.empty()
            ).collectAsLazyPagingItems()

            BookmarksPhotoSection(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(all = Dimens.PADDING_MEDIUM),
                bookmarkedPhotos = photos,
                isAnyPhotoLoading = isPhotoLoading,
                onAnyPhotoClicked = { }
            )
        }
    }
}