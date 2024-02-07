package com.sdv.kit.pexelsapp.domain.repository

import androidx.paging.PagingData
import com.sdv.kit.pexelsapp.domain.model.Bookmarked
import kotlinx.coroutines.flow.Flow

interface BookmarkedRepository {

    suspend fun updateBookmarked(bookmarked: Bookmarked)
    suspend fun getBookmarkedById(photoId: Int): Bookmarked?
    suspend fun getNextPage(): Int
    suspend fun getBookmarkedPhotos(): Flow<PagingData<Bookmarked>>
}