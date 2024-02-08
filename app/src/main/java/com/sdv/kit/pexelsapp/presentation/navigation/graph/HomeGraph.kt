package com.sdv.kit.pexelsapp.presentation.navigation.graph

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import com.sdv.kit.pexelsapp.domain.model.FeaturedCollection
import com.sdv.kit.pexelsapp.domain.model.Photo
import com.sdv.kit.pexelsapp.presentation.home.HomeScreen
import com.sdv.kit.pexelsapp.presentation.home.HomeViewModel
import com.sdv.kit.pexelsapp.presentation.navigation.NavRoute
import com.sdv.kit.pexelsapp.presentation.networkConnectionStatus
import kotlinx.coroutines.flow.flow

fun NavGraphBuilder.homeNavGraph(
    navController: NavHostController,
    isBottomNavigationBarVisible: MutableState<Boolean>,
    activeItemName: MutableState<String>
) {
    composable(
        route = NavRoute.HomeScreen.route +
                "?${NavRoute.SEARCH_QUERY_ARG}={${NavRoute.SEARCH_QUERY_ARG}}" +
                "&${NavRoute.SELECTED_COLLECTION_ARG}={${NavRoute.SELECTED_COLLECTION_ARG}}",
        arguments = listOf(
            navArgument(name = NavRoute.SEARCH_QUERY_ARG) {
                type = NavType.StringType
                defaultValue = ""
            },
            navArgument(name = NavRoute.SELECTED_COLLECTION_ARG) {
                type = NavType.IntType
                defaultValue = -1
            }
        ),
        enterTransition = {
            slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Companion.Right,
                animationSpec = tween(700)
            )
        },
        exitTransition = {
            slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Companion.Left,
                animationSpec = tween(700)
            )
        }
    ) { backStackEntry ->
        activeItemName.value = NavRoute.HomeScreen.route
        isBottomNavigationBarVisible.value = true

        val viewModel: HomeViewModel = hiltViewModel()
        val state by viewModel.state.collectAsState()

        LaunchedEffect(Unit) {
            viewModel.checkIfCachePresents()
            viewModel.getFeaturedCollections()
            viewModel.getPhotos()
        }

        val pagingCollectionItems = (state.collections ?: flow { PagingData.empty<FeaturedCollection>() }).collectAsLazyPagingItems()
        val pagingPhotoItems = (state.photos ?: flow { PagingData.empty<Photo>() }).collectAsLazyPagingItems()
        val searchQuery = state.searchQuery
        val isCachePresents = state.isCachePresents
        val isCacheLoading = state.isCacheLoading
        val selectedFeaturedCollectionIndex = state.selectedFeaturedCollectionIndex

        val searchQueryArg = backStackEntry.arguments?.getString(NavRoute.SEARCH_QUERY_ARG)
        val selectedCollectionArg =
            backStackEntry.arguments?.getInt(NavRoute.SELECTED_COLLECTION_ARG)

        if (!state.isArgsUsed) {
            viewModel.useArguments(
                queryArg = searchQueryArg!!,
                selectedIndexArg = selectedCollectionArg!!
            )
        }

        val isInternetConnected = networkConnectionStatus()
        val isInternetWasDisconnected = remember { mutableStateOf(false) }
        val networkStatusToastsCount = remember { mutableIntStateOf(0) }

        if (!isInternetConnected.value) {
            isInternetWasDisconnected.value = true

            if (!isCacheLoading && networkStatusToastsCount.intValue < 2) {
                networkStatusToastsCount.intValue += 1
            }
        }

        HomeScreen(
            modifier = Modifier.fillMaxSize(),
            onCacheRequest = {
                viewModel.checkIfCachePresents()
                isCachePresents
            },
            isInternetConnected = isInternetConnected.value,
            isInternetWasDisconnected = isInternetWasDisconnected,
            networkStatusToastsCount = networkStatusToastsCount,
            collections = pagingCollectionItems,
            photos = pagingPhotoItems,
            selectedFeaturedCollectionIndex = selectedFeaturedCollectionIndex,
            searchQuery = searchQuery,
            onSearch = { query ->
                viewModel.searchPhotosQuery(query = query)
                viewModel.getPhotos()
            },
            onHttpError = {
                viewModel.getPhotosDelayed()
            },
            onExplorePhotos = {
                viewModel.searchPhotosQuery(query = "")
                viewModel.getPhotos()
            },
            onAnyPhotoClicked = { photo ->
                navController.navigate(
                    route = NavRoute.DetailsScreen(
                        photoId = photo.photoId,
                        screenFrom = NavRoute.HomeScreen.route,
                    ).route +
                            "?${NavRoute.SEARCH_QUERY_ARG}=$searchQuery" +
                            "&${NavRoute.SELECTED_COLLECTION_ARG}=${selectedFeaturedCollectionIndex.intValue}"
                )
            },
            onCollectionsRequest = {
                viewModel.getFeaturedCollections()
            },
            onPhotosRequest = {
                viewModel.getPhotos()
            },
            onTryAgainRequest = {
                viewModel.getPhotos()
            }
        )
    }
}