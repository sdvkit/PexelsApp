package com.sdv.kit.pexelsapp.domain.manager

interface NetworkManager {
    suspend fun checkInternetConnection(onSuccess: () -> Unit, onError: (Exception) -> Unit)
}