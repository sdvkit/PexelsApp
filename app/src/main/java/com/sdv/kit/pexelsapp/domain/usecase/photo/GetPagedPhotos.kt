package com.sdv.kit.pexelsapp.domain.usecase.photo

import androidx.paging.PagingData
import com.sdv.kit.pexelsapp.domain.model.Photo
import com.sdv.kit.pexelsapp.domain.repository.PhotoRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPagedPhotos @Inject constructor(
    private val photoRepository: PhotoRepository
) {

    operator fun invoke(): Flow<PagingData<Photo>> {
        return photoRepository.getPagedPhotos()
    }
}