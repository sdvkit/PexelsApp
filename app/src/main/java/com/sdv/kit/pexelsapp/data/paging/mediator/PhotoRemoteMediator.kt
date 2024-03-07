package com.sdv.kit.pexelsapp.data.paging.mediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.sdv.kit.pexelsapp.data.local.PexelsDatabaseClient
import com.sdv.kit.pexelsapp.data.manager.NetworkManagerImpl
import com.sdv.kit.pexelsapp.data.remote.api.PexelsApi
import com.sdv.kit.pexelsapp.domain.model.Photo

@OptIn(ExperimentalPagingApi::class)
class PhotoRemoteMediator(
    private val pexelsDatabaseClient: PexelsDatabaseClient,
    private val pexelsApi: PexelsApi
) : RemoteMediator<Int, Photo>() {

    private val photoDao = pexelsDatabaseClient.photoDao()
    private var endReached: Boolean = false

    override suspend fun initialize(): InitializeAction {
        var action: InitializeAction? = null

        NetworkManagerImpl.checkInternetConnection(
            onSuccess = { action = InitializeAction.LAUNCH_INITIAL_REFRESH },
            onError = { action = InitializeAction.SKIP_INITIAL_REFRESH }
        )

        return action!!
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, Photo>
    ): MediatorResult {
        return try {
            val page = when (loadType) {
                LoadType.REFRESH -> {
                    null
                }

                LoadType.PREPEND -> {
                    return MediatorResult.Success(endOfPaginationReached = true)
                }

                LoadType.APPEND -> {
                    state.lastItemOrNull()?.page?.inc() ?: 1
                }
            }

            val photoResponse = pexelsApi.getCurated(page = page ?: 1)

            val photos = photoResponse.photos
                .map { photo ->
                    photo.apply {
                        this.page = photoResponse.page
                        lastModified = System.currentTimeMillis()
                    }
                }
                .toTypedArray()

            pexelsDatabaseClient.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    photoDao.clearAll()
                }

                photoDao.insertAll(*photos)
            }

            endReached = photoResponse.nextPage == null

            MediatorResult.Success(endOfPaginationReached = endReached)
        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }
}