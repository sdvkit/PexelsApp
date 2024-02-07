package com.sdv.kit.pexelsapp.domain.repository

import androidx.paging.PagingData
import com.sdv.kit.pexelsapp.domain.model.Photo
import kotlinx.coroutines.flow.Flow

interface PhotoRepository {
    fun getPhotos(): Flow<PagingData<Photo>>
    fun checkIfPhotosInCache(): Boolean
    fun searchPhotos(query: String): Flow<PagingData<Photo>>
    suspend fun getCachedPhotoById(photoId: Int): Photo
    suspend fun getRemotePhotoById(photoId: Int): Photo?
    suspend fun cachePhoto(photo: Photo)
}