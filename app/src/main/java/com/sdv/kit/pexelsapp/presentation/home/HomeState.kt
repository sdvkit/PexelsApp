package com.sdv.kit.pexelsapp.presentation.home

import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.mutableIntStateOf
import androidx.paging.PagingData
import com.sdv.kit.pexelsapp.domain.model.FeaturedCollection
import com.sdv.kit.pexelsapp.domain.model.Photo
import com.sdv.kit.pexelsapp.util.Constants
import kotlinx.coroutines.flow.Flow

data class HomeState(
    val collections: Flow<PagingData<FeaturedCollection>>? = null,
    val photos: Flow<PagingData<Photo>>? = null,
    val isCachePresents: Boolean = false,
    val isCacheLoading: Boolean = true,
    val searchQuery: String = "",
    val isPhotosRequestHandling: Boolean = false,
    val selectedFeaturedCollectionIndex: MutableIntState = mutableIntStateOf(Constants.EMPTY_COLLECTION_HEADER_INDEX),
    val isArgsUsed: Boolean = false
)