package com.sdv.kit.pexelsapp.presentation.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sdv.kit.pexelsapp.domain.model.Bookmarked
import com.sdv.kit.pexelsapp.domain.model.Photo
import com.sdv.kit.pexelsapp.domain.usecase.bookmarked.GetBookmarkedById
import com.sdv.kit.pexelsapp.domain.usecase.bookmarked.GetNextBookmarkedPage
import com.sdv.kit.pexelsapp.domain.usecase.bookmarked.UpdateBookmarked
import com.sdv.kit.pexelsapp.domain.usecase.photo.DownloadPhotoImage
import com.sdv.kit.pexelsapp.domain.usecase.photo.GetRemotePhotoById
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.concurrent.Executors
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val downloadPhotoImageUsecase: DownloadPhotoImage,
    private val getRemotePhotoByIdUsecase: GetRemotePhotoById,
    private val getBookmarkedByIdUsecase: GetBookmarkedById,
    private val updateBookmarkedUsecase: UpdateBookmarked,
    private val getNextBookmarkedPageUsecase: GetNextBookmarkedPage
) : ViewModel() {

    private val _state = MutableStateFlow(DetailsState())
    val state: StateFlow<DetailsState> = _state

    private val photoExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        throwable.printStackTrace()
        _state.value = _state.value.copy(photo = null)
    }

    fun getCachedPhotoInfo(photoId: Int) {
        if (!_state.value.isPhotoLoading) {
            _state.value = _state.value.copy(isPhotoLoading = true)

            viewModelScope.launch(photoExceptionHandler) {
                val bookmarked = getBookmarkedByIdUsecase(photoId = photoId) ?: return@launch
                _state.value = _state.value.copy(
                    photo = bookmarked.photo,
                    isBookmarked = bookmarked.value,
                    isPhotoLoading = false
                )
            }
        }
    }

    fun getRemotePhotoInfo(photoId: Int) {
        if (!_state.value.isPhotoLoading) {
            _state.value = _state.value.copy(isPhotoLoading = true)

            viewModelScope.launch(photoExceptionHandler) {
                val photo = getRemotePhotoByIdUsecase(photoId = photoId) ?: throw RuntimeException()
                val isBookmarked = getBookmarkedByIdUsecase(photoId = photoId)?.value ?: false
                _state.value = _state.value.copy(
                    photo = photo,
                    isBookmarked = isBookmarked,
                    isPhotoLoading = false
                )
            }
        }
    }

    // todo fix creating thread
    fun downloadPhotoImage(
        photo: Photo,
        onError: (Exception) -> Unit
    ) {
        Executors.newSingleThreadExecutor().execute {
            downloadPhotoImageUsecase(
                photo = photo,
                onError = onError
            )
        }
    }

    fun updatePhoto(photo: Photo?) {
        viewModelScope.launch {
            if (photo != null) {
                val photoId = photo.photoId
                val isBookmarked = _state.value.isBookmarked

                val bookmarked = getBookmarkedByIdUsecase(photoId = photoId)
                    ?: Bookmarked(
                        photoId = photoId,
                        value = !isBookmarked,
                        photo = photo,
                        page = getNextBookmarkedPageUsecase()
                    )

                bookmarked.value = !isBookmarked

                updateBookmarkedUsecase(bookmarked = bookmarked)
                _state.value = _state.value.copy(isBookmarked = !isBookmarked)
            }
        }
    }
}