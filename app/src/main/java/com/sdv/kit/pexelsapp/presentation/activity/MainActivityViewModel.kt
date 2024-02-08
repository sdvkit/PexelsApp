package com.sdv.kit.pexelsapp.presentation.activity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.sdv.kit.pexelsapp.domain.usecase.featured.CheckIfCollectionsInCache
import com.sdv.kit.pexelsapp.domain.usecase.featured.GetFeaturedCollections
import com.sdv.kit.pexelsapp.domain.usecase.photo.CheckIfPhotosInCache
import com.sdv.kit.pexelsapp.domain.usecase.photo.GetPhotos
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val getFeaturedCollections: GetFeaturedCollections,
    private val getPhotos: GetPhotos,
    private val checkIfCollectionsInCache: CheckIfCollectionsInCache,
    private val checkIfPhotosInCache: CheckIfPhotosInCache
) : ViewModel() {

    private val _isInternetConnected = MutableStateFlow(false)
    val isInternetConnected: StateFlow<Boolean> = _isInternetConnected

    private val _isFeaturedCollectionsInCache = MutableStateFlow(false)
    private val _isPhotosInCache = MutableStateFlow(false)
    private val _dataChannelsCount = MutableStateFlow(0)
    private val _isDataLoaded = MutableStateFlow(false)

    init {
        viewModelScope.launch(Dispatchers.IO) {
            getFeaturedCollections()
                .cachedIn(viewModelScope)
                .shareIn(
                    scope = CoroutineScope(Dispatchers.IO),
                    started = SharingStarted.Lazily
                )
                .collect {
                    withContext(Dispatchers.IO) {
                        _isFeaturedCollectionsInCache.value = checkIfCollectionsInCache()
                        _dataChannelsCount.value += 1
                    }
                }
        }

        viewModelScope.launch(Dispatchers.IO) {
            getPhotos()
                .cachedIn(viewModelScope)
                .shareIn(
                    scope = CoroutineScope(Dispatchers.IO),
                    started = SharingStarted.Lazily
                )
                .collect {
                    withContext(Dispatchers.IO) {
                        _isPhotosInCache.value = checkIfPhotosInCache()
                        _dataChannelsCount.value += 1
                    }
                }
        }
    }

    fun checkDataIsLoaded(action: (Boolean) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            if (_dataChannelsCount.value >= 2) {
                action(_isPhotosInCache.value && _isFeaturedCollectionsInCache.value)
                _isDataLoaded.value = true
            }
        }
    }

    fun setInternetConnected(isConnected: Boolean) {
        viewModelScope.launch {
            _isInternetConnected.value = isConnected
        }
    }
}