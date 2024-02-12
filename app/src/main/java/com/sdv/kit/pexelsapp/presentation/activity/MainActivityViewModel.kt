package com.sdv.kit.pexelsapp.presentation.activity

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.sdv.kit.pexelsapp.data.manager.NetworkManagerImpl
import com.sdv.kit.pexelsapp.domain.usecase.featured.CacheFeaturedCollections
import com.sdv.kit.pexelsapp.domain.usecase.featured.CheckIfCollectionsInCache
import com.sdv.kit.pexelsapp.domain.usecase.featured.GetFeaturedCollections
import com.sdv.kit.pexelsapp.domain.usecase.photo.CachePhotos
import com.sdv.kit.pexelsapp.domain.usecase.photo.CheckIfPhotosInCache
import com.sdv.kit.pexelsapp.domain.usecase.photo.GetPhotos
import com.sdv.kit.pexelsapp.presentation.navigation.NavRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val getPhotosUsecase: GetPhotos,
    private val getFeaturedCollectionsUsecase: GetFeaturedCollections,
    private val cachePhotosUsecase: CachePhotos,
    private val cacheFeaturedCollectionsUsecase: CacheFeaturedCollections,
    private val checkIfCollectionsInCacheUsecase: CheckIfCollectionsInCache,
    private val checkIfPhotosInCacheUsecase: CheckIfPhotosInCache
) : ViewModel() {

    private val _keepOnSplashScreen = MutableStateFlow(true)
    val keepOnSplashScreen: StateFlow<Boolean> = _keepOnSplashScreen

    private val _startDestination = mutableStateOf<NavRoute>(NavRoute.LoginScreen)
    val startDestination: State<NavRoute> = _startDestination

    init {
        viewModelScope.launch {
            getStartDestination()
        }

        viewModelScope.launch(Dispatchers.IO) {
            NetworkManagerImpl.checkInternetConnection(
                onSuccess = {
                    loadRemoteData()
                },
                onError = {
                    _keepOnSplashScreen.value = false
                }
            )
        }
    }

    private fun loadRemoteData() {
        viewModelScope.launch(Dispatchers.IO) {
            val getPhotosJob = async {
                if (!checkIfPhotosInCacheUsecase()) {
                    val photos = getPhotosUsecase(page = 1)
                    cachePhotosUsecase(photos = photos)
                }
            }

            val getCollectionsJob = async {
                if (!checkIfCollectionsInCacheUsecase()) {
                    val collections = getFeaturedCollectionsUsecase(page = 1)
                    cacheFeaturedCollectionsUsecase(collections = collections)
                }
            }

            awaitAll(getPhotosJob, getCollectionsJob)
            _keepOnSplashScreen.value = false
        }
    }

    private suspend fun getStartDestination() {
        _startDestination.value = when (Firebase.auth.currentUser == null) {
            true -> NavRoute.LoginScreen
            else -> NavRoute.HomeScreen
        }
        delay(300)
        _keepOnSplashScreen.value = false
    }
}