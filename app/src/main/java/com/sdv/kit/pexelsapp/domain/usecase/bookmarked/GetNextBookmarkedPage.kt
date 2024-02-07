package com.sdv.kit.pexelsapp.domain.usecase.bookmarked

import com.sdv.kit.pexelsapp.domain.repository.BookmarkedRepository
import javax.inject.Inject

class GetNextBookmarkedPage @Inject constructor(
    private val bookmarkedRepository: BookmarkedRepository
) {

    suspend operator fun invoke(): Int {
        return bookmarkedRepository.getNextPage()
    }
}