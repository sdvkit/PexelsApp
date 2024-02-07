package com.sdv.kit.pexelsapp.presentation.navigation.graph

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.sdv.kit.pexelsapp.presentation.details.DetailsScreen
import com.sdv.kit.pexelsapp.presentation.details.DetailsViewModel
import com.sdv.kit.pexelsapp.presentation.navigation.NavRoute
import kotlinx.coroutines.launch


fun NavGraphBuilder.detailsGraph(
    navController: NavHostController,
    isBottomNavigationBarVisible: MutableState<Boolean>,
) {
    composable(
        route = NavRoute.DetailsScreen.baseRoute +
                "?${NavRoute.SEARCH_QUERY_ARG}={${NavRoute.SEARCH_QUERY_ARG}}" +
                "&${NavRoute.SELECTED_COLLECTION_ARG}={${NavRoute.SELECTED_COLLECTION_ARG}}",
        arguments = listOf(
            navArgument(name = NavRoute.SCREEN_FROM_ARG) {
                type = NavType.StringType
            },
            navArgument(name = NavRoute.PHOTO_ID_ARG) {
                type = NavType.IntType
            },
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
                towards = AnimatedContentTransitionScope.SlideDirection.Companion.Left,
                animationSpec = tween(700)
            )
        },
        exitTransition = {
            slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Companion.Right,
                animationSpec = tween(700)
            )
        },
        popEnterTransition = {
            slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Companion.Left,
                animationSpec = tween(700)
            )
        },
        popExitTransition = {
            slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Companion.Right,
                animationSpec = tween(700)
            )
        }
    ) { backStackEntry ->
        isBottomNavigationBarVisible.value = false

        val context = LocalContext.current
        val coroutineScope = rememberCoroutineScope()

        val viewModel: DetailsViewModel = hiltViewModel()
        val state by viewModel.state.collectAsState()

        val screenFrom = backStackEntry.arguments?.getString(NavRoute.SCREEN_FROM_ARG)
        val photoId = backStackEntry.arguments?.getInt(NavRoute.PHOTO_ID_ARG)
        val searchQuery = backStackEntry.arguments?.getString(NavRoute.SEARCH_QUERY_ARG)
        val selectedCollection = backStackEntry.arguments?.getInt(NavRoute.SELECTED_COLLECTION_ARG)

        BackHandler {
            navController.navigate(
                route = screenFrom +
                        "?${NavRoute.SEARCH_QUERY_ARG}=${searchQuery}" +
                        "&${NavRoute.SELECTED_COLLECTION_ARG}=${selectedCollection}"
            )
        }

        val photo = state.photo
        val isBookmarked = state.isBookmarked
        val isPhotoLoading = state.isPhotoLoading

        if (photoId != null && photo == null && !isPhotoLoading) {
            when (screenFrom) {
                NavRoute.HomeScreen.route -> {
                    viewModel.getRemotePhotoInfo(photoId = photoId)
                }
                NavRoute.BookmarksScreen.route -> {
                    viewModel.getCachedPhotoInfo(photoId = photoId)
                }
            }
        }

        val launcher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission(),
            onResult = { isGranted ->
                if (isGranted) {
                    viewModel.downloadPhotoImage(
                        photo = photo!!,
                        onError = {  exception ->
                            coroutineScope.launch {
                                Toast.makeText(context, exception.message, Toast.LENGTH_SHORT).show()
                            }
                        }
                    )
                }
            }
        )

        DetailsScreen(
            modifier = Modifier.fillMaxSize(),
            onBackButtonClicked = {
                navController.navigate(
                    route = screenFrom +
                            "?${NavRoute.SEARCH_QUERY_ARG}=${searchQuery}" +
                            "&${NavRoute.SELECTED_COLLECTION_ARG}=${selectedCollection}"
                )
            },
            photo = photo,
            isPhotoLoading = isPhotoLoading,
            onExplorePhotos = {
                navController.navigate(route = NavRoute.HomeScreen.route)
            },
            onDownloadButtonClicked = {
                checkPermission(
                    context = context,
                    onGranted = {
                        viewModel.downloadPhotoImage(
                            photo = photo!!,
                            onError = {  exception ->
                                coroutineScope.launch {
                                    Toast.makeText(context, exception.message, Toast.LENGTH_SHORT).show()
                                }
                            }
                        )
                    },
                    launcher = launcher
                )
            },
            onBookmarkButtonClicked = {
                viewModel.updatePhoto(photo = photo)
            },
            isBookmarkButtonActive = isBookmarked
        )
    }
}

private fun checkPermission(
    context: Context,
    onGranted: () -> Unit,
    launcher: ManagedActivityResultLauncher<String, Boolean>
) {
    when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> {
            when (PackageManager.PERMISSION_GRANTED) {
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.READ_MEDIA_IMAGES
                ) -> onGranted()
                else -> launcher.launch(Manifest.permission.READ_MEDIA_IMAGES)
            }
        }

        Build.VERSION.SDK_INT >= Build.VERSION_CODES.R -> {
            when (PackageManager.PERMISSION_GRANTED) {
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) -> onGranted()
                else -> launcher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }

        else -> {
            when (PackageManager.PERMISSION_GRANTED) {
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) -> onGranted()
                else -> launcher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }
        }
    }
}