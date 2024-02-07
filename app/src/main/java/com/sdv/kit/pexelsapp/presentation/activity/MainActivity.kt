package com.sdv.kit.pexelsapp.presentation.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.sdv.kit.pexelsapp.presentation.navigation.NavGraph
import com.sdv.kit.pexelsapp.presentation.navigation.NavRoute
import com.sdv.kit.pexelsapp.presentation.networkConnectionStatus
import com.sdv.kit.pexelsapp.presentation.ui.theme.AppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<MainActivityViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen().apply {
            setKeepOnScreenCondition(condition = {
                var keepOnSplashScreen = true
                val isInternetConnected = viewModel.isInternetConnected.value

                viewModel.checkDataIsLoaded { isLoaded ->
                    keepOnSplashScreen = when (isLoaded) {
                        true -> false
                        else -> isInternetConnected
                    }
                }

                keepOnSplashScreen
            })
        }

        setContent {
            val isInternetConnected = networkConnectionStatus().value
            viewModel.setInternetConnected(isConnected = isInternetConnected)

            AppTheme {
                NavGraph(startDestination = NavRoute.HomeScreen)
            }
        }
    }
}