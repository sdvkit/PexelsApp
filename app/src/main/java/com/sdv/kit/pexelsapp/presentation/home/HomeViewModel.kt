package com.sdv.kit.pexelsapp.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.sdv.kit.pexelsapp.domain.usecase.featured.CheckIfCollectionsInCache
import com.sdv.kit.pexelsapp.domain.usecase.featured.GetPagedFeaturedCollections
import com.sdv.kit.pexelsapp.domain.usecase.photo.CheckIfPhotosInCache
import com.sdv.kit.pexelsapp.domain.usecase.photo.GetPagedPhotos
import com.sdv.kit.pexelsapp.domain.usecase.photo.SearchPhotos
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getPagedFeaturedCollectionsFromUsecase: GetPagedFeaturedCollections,
    private val getPagedPhotosFromUsecase: GetPagedPhotos,
    private val searchPhotosUsecase: SearchPhotos,
    private val checkIfPhotoInCacheUsecase: CheckIfPhotosInCache,
    private val checkIfCollectionsInCacheUsecase: CheckIfCollectionsInCache
) : ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state

    fun checkIfCachePresents() {
        viewModelScope.launch(Dispatchers.IO) {
            val isPhotoPresents = checkIfPhotoInCacheUsecase()
            val isCollectionPresents = checkIfCollectionsInCacheUsecase()

            _state.value = _state.value.copy(
                isCachePresents = isPhotoPresents || isCollectionPresents,
                isCacheLoading = false
            )
        }
    }

    fun getFeaturedCollections() {
        viewModelScope.launch(Dispatchers.IO) {
            val featuredCollections = getPagedFeaturedCollectionsFromUsecase()
                .cachedIn(viewModelScope)
                .shareIn(
                    scope = CoroutineScope(Dispatchers.IO),
                    started = SharingStarted.Lazily
                )

            _state.value = _state.value.copy(collections = featuredCollections)
        }
    }

    fun getPhotos() {
        viewModelScope.launch(Dispatchers.IO) {
            val photos = when (_state.value.searchQuery.isBlank()) {
                true -> {
                    getPagedPhotosFromUsecase()
                        .cachedIn(viewModelScope)
                        .shareIn(
                            scope = CoroutineScope(Dispatchers.IO),
                            started = SharingStarted.Lazily
                        )
                }
                else -> {
                    searchPhotosUsecase(query = _state.value.searchQuery)
                        .cachedIn(viewModelScope)
                        .shareIn(
                            scope = CoroutineScope(Dispatchers.IO),
                            started = SharingStarted.Lazily
                        )
                }
            }

            _state.value = _state.value.copy(photos = photos)
        }
    }

    fun searchPhotosQuery(query: String) {
        _state.value = _state.value.copy(searchQuery = query)
    }

    fun useArguments(queryArg: String, selectedIndexArg: Int) {
        _state.value.selectedFeaturedCollectionIndex.intValue = selectedIndexArg
        _state.value = _state.value.copy(
            searchQuery = queryArg,
            isArgsUsed = true
        )
    }

    fun getPhotosDelayed() {
        viewModelScope.launch(Dispatchers.IO) {
            if (!_state.value.isPhotosRequestHandling) {
                _state.value = _state.value.copy(isPhotosRequestHandling = true)
                delay(6_000)
                getPhotos()
                _state.value = _state.value.copy(isPhotosRequestHandling = true)
            }
        }
    }
}