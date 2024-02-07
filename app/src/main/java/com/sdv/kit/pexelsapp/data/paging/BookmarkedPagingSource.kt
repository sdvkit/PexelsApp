package com.sdv.kit.pexelsapp.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.sdv.kit.pexelsapp.data.local.PexelsDatabaseClient
import com.sdv.kit.pexelsapp.domain.model.Bookmarked
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class BookmarkedPagingSource(
    pexelsDatabaseClient: PexelsDatabaseClient
) : PagingSource<Int, Bookmarked>() {

    private val bookmarkedDao = pexelsDatabaseClient.bookmarkedDao()

    override fun getRefreshKey(state: PagingState<Int, Bookmarked>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Bookmarked> {
        val page = params.key ?: 1

        return try {
            withContext(Dispatchers.IO) {
                val lastPage = bookmarkedDao.getLastPage()
                val photos = bookmarkedDao.getAllByPage(page = page).distinctBy { it.photoId }

                LoadResult.Page(
                    data = photos,
                    nextKey = if (lastPage == page) null else page + 1,
                    prevKey = null
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
            LoadResult.Error(throwable = e)
        }
    }

}