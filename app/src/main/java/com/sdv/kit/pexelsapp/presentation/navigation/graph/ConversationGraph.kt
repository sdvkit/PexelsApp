package com.sdv.kit.pexelsapp.presentation.navigation.graph

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import com.sdv.kit.pexelsapp.R
import com.sdv.kit.pexelsapp.domain.model.Bookmarked
import com.sdv.kit.pexelsapp.domain.model.MessageType
import com.sdv.kit.pexelsapp.presentation.Dimens
import com.sdv.kit.pexelsapp.presentation.chat.conversation.ConversationScreen
import com.sdv.kit.pexelsapp.presentation.chat.conversation.ConversationViewModel
import com.sdv.kit.pexelsapp.presentation.common.modal.DeleteModal
import com.sdv.kit.pexelsapp.presentation.common.sheet.AttachPhotoBottomSheetContent
import com.sdv.kit.pexelsapp.presentation.common.sheet.ConversationDetailsBottomSheetContent
import com.sdv.kit.pexelsapp.presentation.navigation.NavRoute
import com.sdv.kit.pexelsapp.presentation.ui.theme.AppTheme
import com.sdv.kit.pexelsapp.util.asString
import kotlinx.coroutines.flow.flow

@OptIn(ExperimentalMaterial3Api::class)
fun NavGraphBuilder.conversationNavGraph(
    navController: NavHostController,
    isBottomNavigationBarVisible: MutableState<Boolean>,
) {
    composable(
        route = NavRoute.ConversationScreen.route +
                "?${NavRoute.CLICKED_USER_DETAILS}={${NavRoute.CLICKED_USER_DETAILS}}" +
                "&${NavRoute.CLICKED_CHAT_DETAILS}={${NavRoute.CLICKED_CHAT_DETAILS}}",
        arguments = listOf(
            navArgument(name = NavRoute.CLICKED_USER_DETAILS) {
                type = NavType.StringType
                defaultValue = ""
            },
            navArgument(name = NavRoute.CLICKED_CHAT_DETAILS) {
                type = NavType.StringType
                defaultValue = ""
            }
        ),
        enterTransition = {
            slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Companion.Up,
                animationSpec = tween(700)
            )
        },
        exitTransition = {
            slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Companion.Down,
                animationSpec = tween(700)
            )
        }
    ) { backStackEntry ->
        isBottomNavigationBarVisible.value = false

        BackHandler {
            navController.navigate(
                route = NavRoute.ChatScreen.route
            )
        }

        val viewModel: ConversationViewModel = hiltViewModel()

        val user by viewModel.user
        val chat by viewModel.chat
        val clickedUserDetails by viewModel.clickedUserDetails
        val clickedChatDetails by viewModel.clickedChatDetails
        val bookmarkedPhotos by viewModel.bookmarkedPhotos
        val localImages by viewModel.localImages

        val clickedSerializedUserDetails =
            backStackEntry.arguments?.getString(NavRoute.CLICKED_USER_DETAILS)
        val clickedSerializedChatDetails =
            backStackEntry.arguments?.getString(NavRoute.CLICKED_CHAT_DETAILS)

        var isDetailsBottomSheetVisible by remember { mutableStateOf(false) }
        var isDeleteModalVisible by remember { mutableStateOf(false) }
        var isAttachBottomSheetVisible by remember { mutableStateOf(false) }

        if (clickedSerializedUserDetails?.isNotBlank() == true && chat == null) {
            viewModel.tryGetOldChat(serializedUserDetails = clickedSerializedUserDetails)
            viewModel.deserializeUserDetails(serializedUserDetails = clickedSerializedUserDetails)

            ConversationScreen(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = AppTheme.colors.surface),
                user = user ?: return@composable,
                clickedUserDetails = clickedUserDetails,
                onSendButtonClicked = { message ->
                    viewModel.newChat(
                        friendUserDetails = clickedUserDetails ?: return@ConversationScreen,
                        message = message,
                        messageType = MessageType.TEXT
                    )
                },
                onDetailsButtonClicked = { },
                onAttachButtonClicked = {
                    isAttachBottomSheetVisible = true
                },
                onBackButtonClicked = {
                    navController.navigate(
                        route = NavRoute.ChatScreen.route
                    )
                }
            )
        }

        if (clickedSerializedChatDetails?.isNotBlank() == true) {
            viewModel.deserializeChatDetails(serializedChatDetails = clickedSerializedChatDetails)
            viewModel.getChat(chatDetails = clickedChatDetails ?: return@composable)
        }

        if (chat != null) {
            viewModel.observeMessages()

            ConversationScreen(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = AppTheme.colors.surface),
                user = user ?: return@composable,
                chat = chat,
                clickedChatDetails = clickedChatDetails,
                onSendButtonClicked = { message ->
                    viewModel.sendMessage(
                        message = message,
                        messageType = MessageType.TEXT
                    )
                },
                onDetailsButtonClicked = {
                    isDetailsBottomSheetVisible = true
                },
                onAttachButtonClicked = {
                    isAttachBottomSheetVisible = true
                },
                onBackButtonClicked = {
                    navController.navigate(
                        route = NavRoute.ChatScreen.route
                    )
                }
            )
        }

        if (isDetailsBottomSheetVisible) {
            ModalBottomSheet(
                onDismissRequest = {
                    isDetailsBottomSheetVisible = false
                }
            ) {
                ConversationDetailsBottomSheetContent(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            start = Dimens.PADDING_MEDIUM,
                            end = Dimens.PADDING_MEDIUM,
                            bottom = Dimens.PADDING_BIG
                        ),
                    onDeleteButtonClicked = {
                        isDeleteModalVisible = true
                    }
                )
            }
        }

        if (isAttachBottomSheetVisible) {
            ModalBottomSheet(
                onDismissRequest = {
                    isAttachBottomSheetVisible = false
                }
            ) {
                LaunchedEffect(Unit) {
                    viewModel.getLocalImages()
                    viewModel.getBookmarkedPhotos()
                }

                AttachPhotoBottomSheetContent(
                    modifier = Modifier
                        .weight(0.8f)
                        .fillMaxWidth()
                        .padding(
                            start = Dimens.PADDING_MEDIUM,
                            end = Dimens.PADDING_MEDIUM,
                            bottom = Dimens.PADDING_BIG
                        ),
                    bookmarkedPhotos = (bookmarkedPhotos ?: flow {
                        PagingData.empty<Bookmarked>()
                    }).collectAsLazyPagingItems(),
                    localImages = localImages,
                    onAnyPhotoClicked = { photo ->
                        when (chat) {
                            null -> {
                                viewModel.newChat(
                                    friendUserDetails = clickedUserDetails ?: return@AttachPhotoBottomSheetContent,
                                    message = photo.src.original,
                                    messageType = MessageType.REMOTE_PHOTO
                                )
                            }
                            else -> {
                                viewModel.sendMessage(
                                    message = photo.src.original,
                                    messageType = MessageType.REMOTE_PHOTO
                                )
                            }
                        }

                        isAttachBottomSheetVisible = false
                    },
                    onAnyLocalImageClicked = { imageBitmap ->
                        when (chat) {
                            null -> {
                                viewModel.newChat(
                                    friendUserDetails = clickedUserDetails ?: return@AttachPhotoBottomSheetContent,
                                    message = imageBitmap.asString(),
                                    messageType = MessageType.REMOTE_PHOTO
                                )
                            }
                            else -> {
                                viewModel.sendMessage(
                                    message = imageBitmap.asString(),
                                    messageType = MessageType.LOCAL_PHOTO
                                )
                            }
                        }

                        isAttachBottomSheetVisible = false
                    },
                    onExplore = {
                        navController.navigate(NavRoute.HomeScreen.route)
                    }
                )
            }
        }

        if (isDeleteModalVisible) {
            DeleteModal(
                icon = R.drawable.ic_delete,
                title = R.string.delete_chat,
                text = R.string.delete_chat_description,
                confirmLabel = R.string.delete,
                dismissLabel = R.string.cancel,
                onDismissRequest = {
                    isDeleteModalVisible = false
                },
                onConfirmation = {
                    isDeleteModalVisible = false
                    isDetailsBottomSheetVisible = false

                    viewModel.deleteChat()

                    navController.navigate(
                        route = NavRoute.ChatScreen.route
                    )
                }
            )
        }
    }
}