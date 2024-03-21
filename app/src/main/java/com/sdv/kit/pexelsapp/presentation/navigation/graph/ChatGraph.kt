package com.sdv.kit.pexelsapp.presentation.navigation.graph

import android.widget.Toast
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.google.gson.Gson
import com.sdv.kit.pexelsapp.R
import com.sdv.kit.pexelsapp.domain.model.UserDetails
import com.sdv.kit.pexelsapp.presentation.Dimens
import com.sdv.kit.pexelsapp.presentation.chat.ChatListScreen
import com.sdv.kit.pexelsapp.presentation.chat.ChatViewModel
import com.sdv.kit.pexelsapp.presentation.common.modal.DeleteModal
import com.sdv.kit.pexelsapp.presentation.common.sheet.FriendDetailsBottomSheetContent
import com.sdv.kit.pexelsapp.presentation.common.sheet.NewChatBottomSheetContent
import com.sdv.kit.pexelsapp.presentation.navigation.NavRoute
import com.sdv.kit.pexelsapp.presentation.ui.theme.AppTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
fun NavGraphBuilder.chatNavGraph(
    navController: NavHostController,
    isBottomNavigationBarVisible: MutableState<Boolean>,
    activeItemName: MutableState<String>
) {
    composable(
        route = NavRoute.ChatScreen.route,
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
        isBottomNavigationBarVisible.value = true
        activeItemName.value = NavRoute.ChatScreen.route

        val coroutineScope = rememberCoroutineScope()
        val context = LocalContext.current

        val viewModel: ChatViewModel = hiltViewModel()
        val chats by viewModel.chats
        val friends by viewModel.friends
        val usersSearchState by viewModel.usersSearch
        val user by viewModel.user
        val requestedUsers by viewModel.requestedUsers
        val receivedRequests by viewModel.receivedRequests

        val bottomSheetState = rememberBottomSheetScaffoldState()
        val requestSentToastMessage = stringResource(R.string.request_sent)
        var clickedDetailsInfo: UserDetails? by remember { mutableStateOf(null) }
        var isDeleteModalVisible by remember { mutableStateOf(false) }

        if (clickedDetailsInfo != null) {
            ModalBottomSheet(
                onDismissRequest = {
                    clickedDetailsInfo = null
                }
            ) {
                FriendDetailsBottomSheetContent(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            start = Dimens.PADDING_MEDIUM,
                            end = Dimens.PADDING_MEDIUM,
                            bottom = Dimens.PADDING_BIG
                        ),
                    onDeleteButtonClicked = {
                        isDeleteModalVisible = true
                    },
                    onSendButtonClicked = {
                        navController.navigate(
                            route = NavRoute.ConversationScreen.route +
                                    "?${NavRoute.CLICKED_USER_DETAILS}=${
                                        Gson().toJson(clickedDetailsInfo)
                                    }"
                        )
                    }
                )
            }
        }

        BottomSheetScaffold(
            scaffoldState = bottomSheetState,
            sheetContainerColor = AppTheme.colors.surface,
            sheetShadowElevation = 5.dp,
            sheetPeekHeight = 0.dp,
            sheetContent = {
                NewChatBottomSheetContent(
                    modifier = Modifier
                        .fillMaxHeight(0.9f)
                        .fillMaxWidth()
                        .padding(horizontal = Dimens.PADDING_MEDIUM),
                    currentUser = user ?: UserDetails(),
                    requestedUsers = requestedUsers,
                    onSearch = { query ->
                        viewModel.searchUsers(query = query)
                    },
                    searchState = usersSearchState,
                    onRemoveUserRequestClicked = { cancelableUserDetails ->
                        viewModel.cancelAddFriendRequest(userDetails = cancelableUserDetails)
                    },
                    onAddUserClicked = { clickedUserDetails ->
                        viewModel.sendAddFriendRequest(userDetails = clickedUserDetails)
                        Toast.makeText(context, requestSentToastMessage, Toast.LENGTH_LONG)
                            .show()
                    }
                )

            }
        ) { paddings ->
            ChatListScreen(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddings),
                chatDetailsList = chats,
                onAnyItemClicked = { chatDetails ->
                    navController.navigate(
                        route = NavRoute.ConversationScreen.route +
                                "?${NavRoute.CLICKED_CHAT_DETAILS}=${
                                    Gson().toJson(chatDetails)
                                }"
                    )
                },
                onNewChatButtonClicked = {
                    coroutineScope.launch {
                        clickedDetailsInfo = null
                        bottomSheetState.bottomSheetState.expand()
                    }
                },
                requestedUsers = requestedUsers,
                receivedRequests = receivedRequests,
                friendsList = friends,
                onRemoveUserRequestClicked = { cancelableUserDetails ->
                    viewModel.cancelAddFriendRequest(userDetails = cancelableUserDetails)
                },
                onAcceptRequest = { acceptableUserDetails ->
                    viewModel.acceptRequest(userDetails = acceptableUserDetails)
                },
                onRejectRequest = { cancelableUserDetails ->
                    viewModel.rejectRequest(userDetails = cancelableUserDetails)
                },
                onAnyDetailsClicked = { friendUserDetails ->
                    clickedDetailsInfo = friendUserDetails
                },
                onAnyFriendClicked = { clickedUserDetails ->
                    navController.navigate(
                        route = NavRoute.ConversationScreen.route +
                                "?${NavRoute.CLICKED_USER_DETAILS}=${
                                    Gson().toJson(clickedUserDetails)
                                }"
                    )
                }
            )
        }

        if (isDeleteModalVisible) {
            DeleteModal(
                icon = R.drawable.ic_delete,
                title = R.string.delete_friend,
                text = R.string.delete_friend_description,
                confirmLabel = R.string.delete,
                dismissLabel = R.string.cancel,
                onDismissRequest = {
                    isDeleteModalVisible = false
                },
                onConfirmation = {
                    isDeleteModalVisible = false
                    viewModel.deleteFriend(userDetails = clickedDetailsInfo ?: return@DeleteModal)
                    navController.navigate(route = NavRoute.ChatScreen.route)
                }
            )
        }
    }
}