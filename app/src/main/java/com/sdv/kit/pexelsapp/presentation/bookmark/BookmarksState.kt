package com.sdv.kit.pexelsapp.presentation.bookmark

import androidx.paging.PagingData
import com.sdv.kit.pexelsapp.domain.model.Bookmarked
import kotlinx.coroutines.flow.Flow

data class BookmarksState(
    val bookmarkedPhotos: Flow<PagingData<Bookmarked>>? = null
)