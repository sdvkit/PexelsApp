package com.sdv.kit.pexelsapp.presentation.bookmark

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.sdv.kit.pexelsapp.domain.model.Bookmarked
import com.sdv.kit.pexelsapp.domain.model.Photo
import com.sdv.kit.pexelsapp.presentation.Dimens
import com.sdv.kit.pexelsapp.presentation.annotation.LightAndDarkPreview
import com.sdv.kit.pexelsapp.presentation.bookmark.section.BookmarksPhotoSection
import com.sdv.kit.pexelsapp.presentation.bookmark.section.BookmarksTopBarSection
import com.sdv.kit.pexelsapp.presentation.common.stub.NotSavedStub
import com.sdv.kit.pexelsapp.presentation.ui.theme.AppTheme
import kotlinx.coroutines.flow.MutableStateFlow

@Composable
fun BookmarksScreen(
    modifier: Modifier = Modifier,
    bookmarkedPhotos: LazyPagingItems<Bookmarked>?,
    onAnyPhotoClicked: (Photo) -> Unit,
    onExplorePhotos: () -> Unit
) {
    val isAnyPhotoLoading = remember {
        mutableStateOf(false)
    }

    Column(
        modifier = modifier.background(
            color = AppTheme.colors.surface
        )
    ) {
        Spacer(modifier = Modifier.height(Dimens.PADDING_MEDIUM_SMALLER))
        BookmarksTopBarSection(modifier = Modifier.fillMaxWidth())

        if (isAnyPhotoLoading.value) {
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

        if (bookmarkedPhotos != null) {
            when (bookmarkedPhotos.itemCount == 0) {
                true -> {
                    Spacer(modifier = Modifier.weight(1f))
                    NotSavedStub(
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        onClick = onExplorePhotos
                    )
                    Spacer(modifier = Modifier.weight(1f))
                }
                false -> {
                    Spacer(modifier = Modifier.height(Dimens.PADDING_MEDIUM_BIGGER))
                    BookmarksPhotoSection(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .padding(horizontal = Dimens.PADDING_MEDIUM),
                        bookmarkedPhotos = bookmarkedPhotos,
                        isAnyPhotoLoading = isAnyPhotoLoading,
                        onAnyPhotoClicked = onAnyPhotoClicked
                    )
                }
            }
        }
    }
}

@LightAndDarkPreview
@Composable
fun BookmarksScreenPreview() {
    AppTheme {
        val photos = MutableStateFlow<PagingData<Bookmarked>>(
            PagingData.empty()
        ).collectAsLazyPagingItems()

        BookmarksScreen(
            modifier = Modifier.fillMaxSize(),
            bookmarkedPhotos = photos,
            onAnyPhotoClicked = { },
            onExplorePhotos = {}
        )
    }
}