package com.sdv.kit.pexelsapp.presentation.navigation.graph

import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.sdv.kit.pexelsapp.presentation.login.LoginScreen
import com.sdv.kit.pexelsapp.presentation.login.LoginViewModel
import com.sdv.kit.pexelsapp.presentation.navigation.NavRoute

fun NavGraphBuilder.loginNavGraph(
    navController: NavHostController,
    isBottomNavigationBarVisible: MutableState<Boolean>
) {
    composable(
        route = NavRoute.LoginScreen.route,
        enterTransition = {
            slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Companion.Left,
                animationSpec = tween(700)
            )
        },
        exitTransition = {
            slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Companion.Right,
                animationSpec = tween(700)
            )
        }
    ) {
        isBottomNavigationBarVisible.value = false

        val viewModel: LoginViewModel = hiltViewModel()
        val intentSenderRequest by viewModel.intentSenderRequest
        val authResult by viewModel.authResult
        val errorState by viewModel.errorState

        val context = LocalContext.current

        when (errorState) {
            LoginViewModel.LoginException.LaunchError -> {
                Toast.makeText(context, "Can't launch Sign in process", Toast.LENGTH_SHORT).show()
                viewModel.hideError()
            }

            LoginViewModel.LoginException.SignInError -> {
                Toast.makeText(context, "Can't Sign in", Toast.LENGTH_SHORT).show()
                viewModel.hideError()
            }

            null -> { }
        }

        val googleSignInLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.StartIntentSenderForResult(),
            onResult = { result ->
                if (result.resultCode == ComponentActivity.RESULT_OK) {
                    viewModel.signIn(data = result.data)
                }
            }
        )

        if (intentSenderRequest != null) {
            googleSignInLauncher.launch(intentSenderRequest)
        }

        if (authResult != null) {
            navController.navigate(route = NavRoute.HomeScreen.route) {
                popUpTo(navController.graph.id) {
                    inclusive = true
                }
            }
        }

        LoginScreen(
            modifier = Modifier.fillMaxSize(),
            onGoogleButtonClicked = {
                viewModel.buildIntentSenderRequest()
            }
        )
    }
}