package com.sdv.kit.pexelsapp.domain.usecase.bookmarked

import androidx.paging.PagingData
import com.sdv.kit.pexelsapp.domain.model.Bookmarked
import com.sdv.kit.pexelsapp.domain.repository.BookmarkedRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetBookmarkedPhotos @Inject constructor(
    private val bookmarkedRepository: BookmarkedRepository
) {

    suspend operator fun invoke(): Flow<PagingData<Bookmarked>> {
        return bookmarkedRepository.getBookmarkedPhotos()
    }
}