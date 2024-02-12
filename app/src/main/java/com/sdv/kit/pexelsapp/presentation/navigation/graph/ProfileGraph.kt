package com.sdv.kit.pexelsapp.presentation.navigation.graph

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.sdv.kit.pexelsapp.presentation.navigation.NavRoute
import com.sdv.kit.pexelsapp.presentation.profile.ProfileScreen
import com.sdv.kit.pexelsapp.presentation.profile.ProfileViewModel

fun NavGraphBuilder.profileNavGraph(
    navController: NavHostController,
    isBottomNavigationBarVisible: MutableState<Boolean>,
) {
    composable(
        route = NavRoute.ProfileScreen.route,
        enterTransition = {
            slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Companion.Left,
                animationSpec = tween(700)
            )
        },
        exitTransition = {
            slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Companion.Left,
                animationSpec = tween(700)
            )
        }
    ) {
        isBottomNavigationBarVisible.value = false

        val viewModel: ProfileViewModel = hiltViewModel()
        val profileState by viewModel.profileState

        val isSignedOut = profileState.isSignedOut

        if (isSignedOut) {
            navController.navigate(route = NavRoute.LoginScreen.route) {
                popUpTo(navController.graph.id) {
                    inclusive = true
                }
            }
        }

        val signedInUserDetails = profileState.userDetails ?: return@composable

        ProfileScreen(
            modifier = Modifier.fillMaxSize(),
            userDetails = signedInUserDetails,
            onLogoutClicked = {
                viewModel.logout()
            },
            onBackButtonClicked = {
                navController.navigate(route = NavRoute.HomeScreen.route)
            }
        )
    }
}