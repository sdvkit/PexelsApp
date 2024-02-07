package com.sdv.kit.pexelsapp.presentation

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.sdv.kit.pexelsapp.data.manager.NetworkManagerImpl

@Composable
fun networkConnectionStatus(): MutableState<Boolean> {
    val isConnected = remember {
        mutableStateOf(true)
    }

    val context = LocalContext.current
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    val connectivityCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            isConnected.value = true
        }

        override fun onLost(network: Network) {
            isConnected.value = false
        }
    }

    DisposableEffect(connectivityManager) {
        connectivityManager.registerDefaultNetworkCallback(connectivityCallback)

        onDispose {
            connectivityManager.unregisterNetworkCallback(connectivityCallback)
        }
    }

    LaunchedEffect(Unit) {
        NetworkManagerImpl.checkInternetConnection(
            onSuccess = { isConnected.value = true },
            onError = { isConnected.value = false }
        )
    }

    return isConnected
}