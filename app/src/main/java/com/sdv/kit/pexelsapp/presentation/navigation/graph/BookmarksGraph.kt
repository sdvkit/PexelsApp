package com.sdv.kit.pexelsapp.presentation.navigation.graph

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.paging.compose.collectAsLazyPagingItems
import com.sdv.kit.pexelsapp.presentation.bookmark.BookmarksScreen
import com.sdv.kit.pexelsapp.presentation.bookmark.BookmarksViewModel
import com.sdv.kit.pexelsapp.presentation.navigation.NavRoute

fun NavGraphBuilder.bookmarksNavGraph(
    navController: NavHostController,
    isBottomNavigationBarVisible: MutableState<Boolean>,
    activeItemName: MutableState<String>
) {
    composable(
        route = NavRoute.BookmarksScreen.route,
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
        activeItemName.value = NavRoute.BookmarksScreen.route
        isBottomNavigationBarVisible.value = true

        val viewModel: BookmarksViewModel = hiltViewModel()
        val state by viewModel.state.collectAsState()

        LaunchedEffect(Unit) {
            viewModel.getBookmarkedPhotos()
        }

        val photos = state.bookmarkedPhotos

        BookmarksScreen(
            modifier = Modifier.fillMaxSize(),
            bookmarkedPhotos = photos?.collectAsLazyPagingItems(),
            onAnyPhotoClicked = { photo ->
                navController.navigate(
                    route = NavRoute.DetailsScreen(
                        photoId = photo.photoId,
                        screenFrom = NavRoute.BookmarksScreen.route
                    ).route
                )
            },
            onExplorePhotos = {
                activeItemName.value = NavRoute.HomeScreen.route
                navController.navigate(route = NavRoute.HomeScreen.route)
            }
        )
    }
}