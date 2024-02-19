package com.sdv.kit.pexelsapp.presentation.navigation.graph

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
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
import com.sdv.kit.pexelsapp.presentation.common.modal.RequestPermissionRationaleModal
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

        val context = LocalContext.current
        val viewModel: ProfileViewModel = hiltViewModel()

        viewModel.getLocalImages()
        getStorageRequiredPermissions().forEach { permission ->
            viewModel.getStoragePermissionEntry(permission = permission)
        }
        getCameraRequiredPermissions().forEach { permission ->
            viewModel.getCameraPermissionEntry(permission = permission)
        }

        val profileState by viewModel.profileState

        val isSignedOut = profileState.isSignedOut
        val localImages = profileState.localImages
        val lastStoragePermissionEntry = profileState.lastStoragePermissionEntry
        val lastCameraPermissionEntry = profileState.lastCameraPermissionEntry

        if (isSignedOut) {
            navController.navigate(route = NavRoute.LoginScreen.route) {
                popUpTo(navController.graph.id) {
                    inclusive = true
                }
            }
        }

        val signedInUserDetails = profileState.userDetails ?: return@composable

        var denyStoragePermissionsCount by remember { mutableIntStateOf(0) }
        val shouldShowStorageRequestPermissionRationale = remember { mutableStateOf(false) }
        var isStoragePermissionGranted by remember { mutableStateOf(false) }
        isStoragePermissionGranted = hasStoragePermissions(context = context)

        var denyCameraPermissionsCount by remember { mutableIntStateOf(0) }
        val shouldShowCameraRequestPermissionRationale = remember { mutableStateOf(false) }
        var isCameraPermissionGranted by remember { mutableStateOf(false) }
        isCameraPermissionGranted = hasCameraPermission(context = context)

        val cameraPermissionLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            val isAllGranted = permissions.values.none { isGranted -> !isGranted }

            when {
                isAllGranted -> {
                    isCameraPermissionGranted = true
                    navController.navigate(route = NavRoute.CameraScreen.route)
                }
                else -> denyCameraPermissionsCount += 1
            }
        }

        val settingsLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.StartActivityForResult(),
            onResult = { }
        )

        val storagePermissionsLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            val isAllGranted = permissions.values.none { isGranted -> !isGranted }

            when {
                isAllGranted -> isStoragePermissionGranted = true
                else -> denyStoragePermissionsCount += 1
            }
        }

        StorageRequestPermissionRationaleModals(
            isStoragePermissionGranted = isStoragePermissionGranted,
            lastStoragePermissionEntry = lastStoragePermissionEntry,
            denyStoragePermissionsCount = denyStoragePermissionsCount,
            viewModel = viewModel,
            shouldShowRequestPermissionRationale = shouldShowStorageRequestPermissionRationale,
            storagePermissionsLauncher = storagePermissionsLauncher,
            settingsLauncher = settingsLauncher
        )

        CameraRequestPermissionRationaleModals(
            isCameraPermissionGranted = isCameraPermissionGranted,
            lastCameraPermissionEntry = lastCameraPermissionEntry,
            denyCameraPermissionsCount = denyCameraPermissionsCount,
            viewModel = viewModel,
            shouldShowCameraRequestPermissionRationale = shouldShowCameraRequestPermissionRationale,
            cameraPermissionLauncher = cameraPermissionLauncher,
            settingsLauncher = settingsLauncher
        )

        ProfileScreen(
            modifier = Modifier.fillMaxSize(),
            isStoragePermissionGranted = isStoragePermissionGranted,
            onPermissionRequest = {
                checkStoragePermission(
                    context = context,
                    onGranted = {
                        isStoragePermissionGranted = true
                    },
                    onNotGranted = {
                        storagePermissionsLauncher.launch(getStorageRequiredPermissions().toTypedArray())
                    },
                    onShouldShowRequestPermissionRationale = {
                        if (lastStoragePermissionEntry?.second == false && denyStoragePermissionsCount == 0) {
                            storagePermissionsLauncher.launch(getStorageRequiredPermissions().toTypedArray())
                            return@checkStoragePermission
                        }

                        shouldShowStorageRequestPermissionRationale.value = true
                    }
                )
            },
            userDetails = signedInUserDetails,
            onLogoutClicked = {
                viewModel.logout()
            },
            onBackButtonClicked = {
                navController.navigate(route = NavRoute.HomeScreen.route)
            },
            onTakePhotoClicked = {
                checkCameraPermission(
                    context = context,
                    onGranted = {
                        isCameraPermissionGranted = true
                    },
                    onNotGranted = {
                        cameraPermissionLauncher.launch(getCameraRequiredPermissions().toTypedArray())
                    },
                    onShouldShowRequestPermissionRationale = {
                        if (lastCameraPermissionEntry?.second == false && denyCameraPermissionsCount == 0) {
                            cameraPermissionLauncher.launch(getCameraRequiredPermissions().toTypedArray())
                            return@checkCameraPermission
                        }

                        shouldShowCameraRequestPermissionRationale.value = true
                    }
                )
            },
            images = localImages
        )
    }
}

@Composable
private fun StorageRequestPermissionRationaleModals(
    isStoragePermissionGranted: Boolean,
    lastStoragePermissionEntry: Pair<String, Boolean>?,
    denyStoragePermissionsCount: Int,
    viewModel: ProfileViewModel,
    shouldShowRequestPermissionRationale: MutableState<Boolean>,
    storagePermissionsLauncher: ManagedActivityResultLauncher<Array<String>, Map<String, @JvmSuppressWildcards Boolean>>,
    settingsLauncher: ManagedActivityResultLauncher<Intent, ActivityResult>
) {
    if (!isStoragePermissionGranted && lastStoragePermissionEntry?.second == false && denyStoragePermissionsCount == 1) {
        RequestPermissionRationaleModal(
            title = R.string.need_permission_request_dialog_title,
            text = R.string.storage_permission_request_dialod_text,
            confirmLabel = R.string.ok,
            onConfirmation = {
                viewModel.saveStoragePermissionEntry(permission = lastStoragePermissionEntry.first)
                storagePermissionsLauncher.launch(getStorageRequiredPermissions().toTypedArray())
            }
        )
    }

    if (!isStoragePermissionGranted && shouldShowRequestPermissionRationale.value) {
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
                shouldShowRequestPermissionRationale.value = false
            },
            onDismissRequest = {
                shouldShowRequestPermissionRationale.value = false
            }
        )
    }
}

@Composable
private fun CameraRequestPermissionRationaleModals(
    isCameraPermissionGranted: Boolean,
    lastCameraPermissionEntry: Pair<String, Boolean>?,
    denyCameraPermissionsCount: Int,
    viewModel: ProfileViewModel,
    shouldShowCameraRequestPermissionRationale: MutableState<Boolean>,
    cameraPermissionLauncher: ManagedActivityResultLauncher<Array<String>, Map<String, @JvmSuppressWildcards Boolean>>,
    settingsLauncher: ManagedActivityResultLauncher<Intent, ActivityResult>
) {
    if (!isCameraPermissionGranted && lastCameraPermissionEntry?.second == false && denyCameraPermissionsCount == 1) {
        RequestPermissionRationaleModal(
            title = R.string.need_permission_request_dialog_title,
            text = R.string.camera_permission_request_dialod_text,
            confirmLabel = R.string.ok,
            onConfirmation = {
                viewModel.saveCameraPermissionEntry(permission = lastCameraPermissionEntry.first)
                cameraPermissionLauncher.launch(getStorageRequiredPermissions().toTypedArray())
            }
        )
    }

    if (!isCameraPermissionGranted && shouldShowCameraRequestPermissionRationale.value) {
        RequestPermissionRationaleModal(
            icon = R.drawable.ic_camera,
            title = R.string.storage_permission_request_dialod_title,
            text = R.string.camera_permission_request_dialod_text,
            confirmLabel = R.string.go_to_settings,
            dismissLabel = R.string.cancel,
            onConfirmation = {
                val intent = Intent(Settings.ACTION_SETTINGS)
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

                settingsLauncher.launch(intent)
                shouldShowCameraRequestPermissionRationale.value = false
            },
            onDismissRequest = {
                shouldShowCameraRequestPermissionRationale.value = false
            }
        )
    }
}

private fun hasCameraPermission(context: Context): Boolean {
    return ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.CAMERA
    ) == PackageManager.PERMISSION_GRANTED
}

private fun hasStoragePermissions(context: Context): Boolean {
    return getStorageRequiredPermissions().all { permission ->
        ContextCompat.checkSelfPermission(
            context,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }
}

private fun checkCameraPermission(
    context: Context,
    onGranted: () -> Unit,
    onNotGranted: (String) -> Unit,
    onShouldShowRequestPermissionRationale: () -> Unit
) {
    getCameraRequiredPermissions().forEach { permission ->
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

private fun getCameraRequiredPermissions(): List<String> {
    return listOf(Manifest.permission.CAMERA)
}