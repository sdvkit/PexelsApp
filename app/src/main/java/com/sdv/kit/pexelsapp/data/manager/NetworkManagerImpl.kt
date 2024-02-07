package com.sdv.kit.pexelsapp.data.manager

import com.sdv.kit.pexelsapp.domain.manager.NetworkManager
import com.sdv.kit.pexelsapp.util.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Socket

object NetworkManagerImpl : NetworkManager {

    override suspend fun checkInternetConnection(
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit
    ) {
        withContext(Dispatchers.IO) {
            try {
                val socket = Socket()
                val timeoutMs = Constants.CHECK_INTERNET_TIMEOUT

                val socketAddress = InetSocketAddress(
                    Constants.CHECK_INTERNET_HOST,
                    Constants.CHECK_INTERNET_PORT
                )

                socket.connect(socketAddress, timeoutMs)
                socket.close()

                onSuccess()
            } catch (e: IOException) {
                onError(e)
            }
        }
    }
}