package com.sdv.kit.pexelsapp.data.paging.mediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.sdv.kit.pexelsapp.data.local.PexelsDatabaseClient
import com.sdv.kit.pexelsapp.data.manager.NetworkManagerImpl
import com.sdv.kit.pexelsapp.data.remote.PexelsApi
import com.sdv.kit.pexelsapp.domain.model.FeaturedCollection
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalPagingApi::class)
class FeaturedCollectionRemoteMediator(
    private val pexelsDatabaseClient: PexelsDatabaseClient,
    private val pexelsApi: PexelsApi
) : RemoteMediator<Int, FeaturedCollection>() {

    private val featuredCollectionDao = pexelsDatabaseClient.featuredCollectionDao()
    private var endReached: Boolean = false

    override suspend fun initialize(): InitializeAction {
        var action: InitializeAction? = null
        val cacheTimeout = TimeUnit.HOURS.convert(1, TimeUnit.MILLISECONDS)
        val isCacheExpired = System.currentTimeMillis() - featuredCollectionDao.lastUpdated() >= cacheTimeout

        NetworkManagerImpl.checkInternetConnection(
            onSuccess = { action = InitializeAction.LAUNCH_INITIAL_REFRESH },
            onError = { action = InitializeAction.SKIP_INITIAL_REFRESH }
        )

        return action!!
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, FeaturedCollection>
    ): MediatorResult {
        return try {
            when (loadType) {
                LoadType.REFRESH -> {
                    featuredCollectionDao.clearAll()
                }
                LoadType.PREPEND -> {
                    return MediatorResult.Success(endOfPaginationReached = true)
                }
                LoadType.APPEND -> {
                    val loadKey = state.lastItemOrNull()?.page?.inc() ?: 1
                    val featuredCollectionsResponse = pexelsApi.getFeatured(page = loadKey)

                    pexelsDatabaseClient.withTransaction {
                        val featuredCollections = featuredCollectionsResponse.featuredCollections
                            .map { featuredCollection ->
                                featuredCollection.apply {
                                    page = featuredCollectionsResponse.page
                                    lastModified = System.currentTimeMillis()
                                }
                            }
                            .toTypedArray()

                        featuredCollectionDao.insertAll(*featuredCollections)
                    }

                    endReached = featuredCollectionsResponse.nextPage == null
                }
            }

            MediatorResult.Success(endOfPaginationReached = endReached)
        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }
}