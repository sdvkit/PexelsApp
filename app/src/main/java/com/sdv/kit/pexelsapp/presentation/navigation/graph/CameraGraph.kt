package com.sdv.kit.pexelsapp.presentation.navigation.graph

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Settings
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.sdv.kit.pexelsapp.R
import com.sdv.kit.pexelsapp.presentation.camera.CameraScreen
import com.sdv.kit.pexelsapp.presentation.camera.CameraViewModel
import com.sdv.kit.pexelsapp.presentation.camera.section.CameraResultSection
import com.sdv.kit.pexelsapp.presentation.common.modal.RequestPermissionRationaleModal
import com.sdv.kit.pexelsapp.presentation.navigation.NavRoute

fun NavGraphBuilder.cameraGraph(
    navController: NavHostController,
    isBottomNavigationBarVisible: MutableState<Boolean>,
) {
    composable(
        route = NavRoute.CameraScreen.route,
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
    ) {
        isBottomNavigationBarVisible.value = false

        val viewModel: CameraViewModel = hiltViewModel()
        val state by viewModel.state

        getStorageRequiredPermissions().forEach { permission ->
            viewModel.getStoragePermissionEntry(permission = permission)
        }

        val lastBitmap = state.lastBitmap
        val errorMessage = state.errorMessage
        val lastPermissionEntry = state.lastPermissionEntry

        BackHandler {
            viewModel.cancelSave()
        }

        val settingsLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.StartActivityForResult()
        ) { }

        val context = LocalContext.current
        val cameraController = remember {
            LifecycleCameraController(context).apply {
                setEnabledUseCases(CameraController.IMAGE_CAPTURE)
            }
        }

        var isStoragePermissionGranted by remember { mutableStateOf(false) }
        var storagePermissionsDenyCount by remember { mutableIntStateOf(0) }
        var shouldShowRequestPermissionRationale by remember { mutableStateOf(false) }

        val storagePermissionsLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            isStoragePermissionGranted = permissions.values.none { isGranted -> !isGranted }

            when {
                isStoragePermissionGranted -> {
                    viewModel.savePhoto()

                    Toast.makeText(
                        context,
                        context.getString(R.string.photo_saved),
                        Toast.LENGTH_SHORT
                    ).show()
                }

                else -> {
                    storagePermissionsDenyCount += 1
                }
            }
        }

        if (!isStoragePermissionGranted && lastPermissionEntry?.second == false && storagePermissionsDenyCount == 1) {
            RequestPermissionRationaleModal(
                title = R.string.need_permission_request_dialog_title,
                text = R.string.storage_permission_request_dialod_text,
                confirmLabel = R.string.ok,
                onConfirmation = {
                    viewModel.savePermissionEntry(permission = lastPermissionEntry.first)
                    storagePermissionsLauncher.launch(getStorageRequiredPermissions().toTypedArray())
                }
            )
        }

        if (!isStoragePermissionGranted && shouldShowRequestPermissionRationale) {
            RequestPermissionRationaleModal(
                icon = R.drawable.ic_gallery,
                title = R.string.storage_permission_request_dialod_title,
                text = R.string.storage_permission_request_dialod_text,
                confirmLabel = R.string.go_to_settings,
                dismissLabel = R.string.cancel,
                onConfirmation = {
                    val intent = Intent(Settings.ACTION_SETTINGS)
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

                    settingsLauncher.launch(intent)
                    shouldShowRequestPermissionRationale = false
                },
                onDismissRequest = {
                    shouldShowRequestPermissionRationale = false
                }
            )
        }

        if (errorMessage != null) {
            Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
        }

        AnimatedVisibility(
            visible = lastBitmap != null,
            enter = scaleIn(),
            exit = scaleOut()
        ) {
            CameraResultSection(
                modifier = Modifier.fillMaxSize(),
                bitmap = lastBitmap ?: return@AnimatedVisibility,
                onSaveButtonClicked = {
                    checkStoragePermission(
                        context = context,
                        onGranted = {
                            viewModel.savePhoto()

                            Toast.makeText(
                                context,
                                context.getString(R.string.photo_saved),
                                Toast.LENGTH_SHORT
                            ).show()
                        },
                        onNotGranted = {
                            storagePermissionsLauncher.launch(getStorageRequiredPermissions().toTypedArray())
                        },
                        onShouldShowRequestPermissionRationale = {
                            if (lastPermissionEntry?.second == false && storagePermissionsDenyCount == 0) {
                                storagePermissionsLauncher.launch(getStorageRequiredPermissions().toTypedArray())
                                return@checkStoragePermission
                            }

                            shouldShowRequestPermissionRationale = true
                        }
                    )
                },
                onCancelButtonClicked = {
                    viewModel.cancelSave()
                }
            )
        }

        if (lastBitmap == null) {
            CameraScreen(
                modifier = Modifier.fillMaxSize(),
                controller = cameraController,
                onBackButtonClicked = {
                    navController.navigateUp()
                },
                onTakePhoto = {
                    viewModel.takePhoto(controller = cameraController)
                }
            )
        }
    }
}

private fun checkStoragePermission(
    context: Context,
    onGranted: () -> Unit,
    onNotGranted: (String) -> Unit,
    onShouldShowRequestPermissionRationale: () -> Unit
) {
    getStorageRequiredPermissions().forEach { permission ->
        when {
            ContextCompat.checkSelfPermission(
                context,
                permission
            ) == PackageManager.PERMISSION_GRANTED -> {
                onGranted()
            }

            ActivityCompat.shouldShowRequestPermissionRationale(
                context as Activity, permission
            ) -> {
                onShouldShowRequestPermissionRationale()
            }

            else -> {
                onNotGranted(permission)
            }
        }
    }
}

private fun getStorageRequiredPermissions(): List<String> {
    return when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> {
            listOf(Manifest.permission.READ_MEDIA_IMAGES)
        }

        Build.VERSION.SDK_INT >= Build.VERSION_CODES.R -> {
            listOf(Manifest.permission.READ_EXTERNAL_STORAGE)
        }

        else -> {
            listOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
    }
}