package com.sdv.kit.pexelsapp.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.sdv.kit.pexelsapp.data.local.PexelsDatabaseClient
import com.sdv.kit.pexelsapp.data.paging.BookmarkedPagingSource
import com.sdv.kit.pexelsapp.domain.model.Bookmarked
import com.sdv.kit.pexelsapp.domain.repository.BookmarkedRepository
import com.sdv.kit.pexelsapp.util.Constants
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class BookmarkedRepositoryImpl @Inject constructor(
    private val pexelsDatabaseClient: PexelsDatabaseClient
) : BookmarkedRepository {

    private val bookmarkedDao = pexelsDatabaseClient.bookmarkedDao()

    override suspend fun updateBookmarked(bookmarked: Bookmarked) {
        return bookmarkedDao.insertAll(bookmarked)
    }

    override suspend fun getBookmarkedById(photoId: Int): Bookmarked? {
        return bookmarkedDao.getById(photoId = photoId)
    }

    override suspend fun getNextPage(): Int {
        return (bookmarkedDao.getCount() / Constants.PHOTOS_PER_PAGE) + 1
    }

    override suspend fun getBookmarkedPhotos(): Flow<PagingData<Bookmarked>> {
        return Pager(
            config = PagingConfig(pageSize = Constants.PHOTOS_PER_PAGE),
            pagingSourceFactory = {
                BookmarkedPagingSource(pexelsDatabaseClient = pexelsDatabaseClient)
            }
        ).flow
    }
}