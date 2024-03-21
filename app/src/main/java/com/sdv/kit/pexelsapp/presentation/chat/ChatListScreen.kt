package com.sdv.kit.pexelsapp.presentation.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.sdv.kit.pexelsapp.domain.model.ChatDetails
import com.sdv.kit.pexelsapp.domain.model.UserDetails
import com.sdv.kit.pexelsapp.presentation.Dimens
import com.sdv.kit.pexelsapp.presentation.annotation.LightAndDarkPreview
import com.sdv.kit.pexelsapp.presentation.chat.section.ChatContentSection
import com.sdv.kit.pexelsapp.presentation.chat.section.ChatsTopBarSection
import com.sdv.kit.pexelsapp.presentation.ui.theme.AppTheme

@Composable
fun ChatListScreen(
    modifier: Modifier = Modifier,
    friendsList: List<UserDetails>,
    chatDetailsList: List<ChatDetails>,
    requestedUsers: List<UserDetails>,
    receivedRequests: List<UserDetails>,
    onRemoveUserRequestClicked: (UserDetails) -> Unit,
    onNewChatButtonClicked: () -> Unit,
    onAnyItemClicked: (ChatDetails) -> Unit,
    onAnyFriendClicked: (UserDetails) -> Unit,
    onRejectRequest: (UserDetails) -> Unit,
    onAcceptRequest: (UserDetails) -> Unit,
    onAnyDetailsClicked: (UserDetails) -> Unit
) {
    Column(modifier = modifier) {
        Spacer(modifier = Modifier.height(Dimens.PADDING_MEDIUM_SMALLER))
        ChatsTopBarSection(modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(Dimens.PADDING_MEDIUM_SMALLER))
        ChatContentSection(
            chatDetailsList = chatDetailsList,
            onNewChatButtonClicked = onNewChatButtonClicked,
            onAnyChatClicked = onAnyItemClicked,
            requestedUsers = requestedUsers,
            onRemoveUserRequestClicked = onRemoveUserRequestClicked,
            receivedRequests = receivedRequests,
            onAcceptRequest = onAcceptRequest,
            onRejectRequest = onRejectRequest,
            friendsList = friendsList,
            onAnyFriendClicked = onAnyFriendClicked,
            onAnyDetailsClicked = onAnyDetailsClicked
        )
    }
}

@LightAndDarkPreview
@Composable
fun ChatListScreenPreview() {
    AppTheme {
        ChatListScreen(
            modifier = Modifier
                .fillMaxSize()
                .background(color = AppTheme.colors.surface),
            chatDetailsList = listOf(
                ChatDetails(
                    chatId = "0",
                    chatName = "MyFavChat",
                    chatImageUrl = "",
                    lastMessage = "Last message 1"
                ),
                ChatDetails(
                    chatId = "1",
                    chatName = "MySecondChat",
                    chatImageUrl = "",
                    lastMessage = "Last message 2"
                )
            ),
            receivedRequests = listOf(
                UserDetails(
                    username = "Pavel Krotov",
                    email = "pavel@gmail.com"
                ),
                UserDetails(
                    username = "Artem Vasilevich",
                    email = "artem@gmail.com"
                )
            ),
            onNewChatButtonClicked = { },
            onAnyItemClicked = { },
            requestedUsers = emptyList(),
            onRemoveUserRequestClicked = { },
            onRejectRequest = { },
            onAcceptRequest = { },
            friendsList = emptyList(),
            onAnyFriendClicked = { },
            onAnyDetailsClicked = { }
        )
    }
}