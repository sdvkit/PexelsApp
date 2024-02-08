package com.sdv.kit.pexelsapp.presentation.bookmark

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.sdv.kit.pexelsapp.domain.usecase.bookmarked.GetBookmarkedPhotos
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookmarksViewModel @Inject constructor(
    private val getBookmarkedPhotosUsecase: GetBookmarkedPhotos
) : ViewModel() {

    private val _state = MutableStateFlow(BookmarksState())
    val state: StateFlow<BookmarksState> = _state

    fun getBookmarkedPhotos() {
        viewModelScope.launch(Dispatchers.IO) {
            val bookmarkedPhotos = getBookmarkedPhotosUsecase()
                .cachedIn(viewModelScope)

            _state.value = _state.value.copy(bookmarkedPhotos = bookmarkedPhotos)
        }
    }
}