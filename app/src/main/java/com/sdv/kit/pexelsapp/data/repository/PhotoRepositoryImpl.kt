package com.sdv.kit.pexelsapp.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.sdv.kit.pexelsapp.data.local.PexelsDatabaseClient
import com.sdv.kit.pexelsapp.data.paging.mediator.SearchPhotoRemoteMediator
import com.sdv.kit.pexelsapp.data.remote.api.PexelsApi
import com.sdv.kit.pexelsapp.domain.model.Photo
import com.sdv.kit.pexelsapp.domain.repository.PhotoRepository
import com.sdv.kit.pexelsapp.util.Constants
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PhotoRepositoryImpl @Inject constructor(
    private val photoPager: Pager<Int, Photo>,
    private val pexelsDatabaseClient: PexelsDatabaseClient,
    private val pexelsApi: PexelsApi
) : PhotoRepository {

    private val photoDao = pexelsDatabaseClient.photoDao()

    override fun getPagedPhotos(): Flow<PagingData<Photo>> {
        return photoPager.flow
    }

    override suspend fun getPhotos(page: Int): List<Photo> {
        return pexelsApi.getCurated(page = page).photos
    }

    override fun checkIfPhotosInCache(): Boolean {
        return photoDao.getCount() > 0
    }

    @OptIn(ExperimentalPagingApi::class)
    override fun searchPhotos(query: String): Flow<PagingData<Photo>> {
        return Pager(
            config = PagingConfig(pageSize = Constants.PAGE_CONFIG_PHOTO_SIZE),
            remoteMediator = SearchPhotoRemoteMediator(
                pexelsDatabaseClient = pexelsDatabaseClient,
                pexelsApi = pexelsApi,
                query = query
            ),
            pagingSourceFactory = {
                pexelsDatabaseClient.photoDao().pagingSource()
            }
        ).flow
    }

    override suspend fun getCachedPhotoById(photoId: Int): Photo {
        return photoDao.getById(photoId = photoId)
    }

    override suspend fun getRemotePhotoById(photoId: Int): Photo? {
        return pexelsApi.getPhotoById(id = photoId)
    }

    override suspend fun cachePhoto(photo: Photo) {
        photoDao.insertAll(photo)
    }

    override suspend fun cachePhotos(photos: List<Photo>) {
        photoDao.insertAll(*photos.toTypedArray())
    }
}