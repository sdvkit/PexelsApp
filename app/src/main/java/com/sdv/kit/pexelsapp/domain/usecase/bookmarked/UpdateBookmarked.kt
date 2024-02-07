package com.sdv.kit.pexelsapp.domain.usecase.bookmarked

import com.sdv.kit.pexelsapp.domain.model.Bookmarked
import com.sdv.kit.pexelsapp.domain.repository.BookmarkedRepository
import javax.inject.Inject

class UpdateBookmarked @Inject constructor(
    private val bookmarkedRepository: BookmarkedRepository
) {

    suspend operator fun invoke(bookmarked: Bookmarked) {
       bookmarkedRepository.updateBookmarked(bookmarked = bookmarked)
    }
}