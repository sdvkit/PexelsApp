package com.sdv.kit.pexelsapp.presentation.chat.section

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import com.sdv.kit.pexelsapp.R
import com.sdv.kit.pexelsapp.domain.model.ChatDetails
import com.sdv.kit.pexelsapp.domain.model.UserDetails
import com.sdv.kit.pexelsapp.presentation.Dimens
import com.sdv.kit.pexelsapp.presentation.annotation.LightAndDarkPreview
import com.sdv.kit.pexelsapp.presentation.common.SearchBar
import com.sdv.kit.pexelsapp.presentation.common.item.ChatListItem
import com.sdv.kit.pexelsapp.presentation.ui.theme.AppTheme
import com.sdv.kit.pexelsapp.presentation.ui.theme.White

@Composable
fun ChatContentSection(
    modifier: Modifier = Modifier,
    friendsList: List<UserDetails>,
    chatDetailsList: List<ChatDetails>,
    requestedUsers: List<UserDetails>,
    receivedRequests: List<UserDetails>,
    onRemoveUserRequestClicked: (UserDetails) -> Unit,
    onNewChatButtonClicked: () -> Unit,
    onAnyChatClicked: (ChatDetails) -> Unit,
    onAnyFriendClicked: (UserDetails) -> Unit,
    onRejectRequest: (UserDetails) -> Unit,
    onAcceptRequest: (UserDetails) -> Unit,
    onAnyDetailsClicked: (UserDetails) -> Unit
) {
    val searchValue = remember { mutableStateOf(TextFieldValue(text = "")) }

    var tabIndex by remember { mutableIntStateOf(0) }
    val tabs = listOf(R.string.chats, R.string.friends)

    Box(modifier = modifier) {
        Column {
            Column(modifier = Modifier.fillMaxWidth()) {
                TabRow(
                    selectedTabIndex = tabIndex,
                    containerColor = AppTheme.colors.surface,
                    contentColor = AppTheme.colors.primary,
                    indicator = { tabPositions ->
                        TabRowDefaults.Indicator(
                            modifier = Modifier.tabIndicatorOffset(tabPositions[tabIndex]),
                            color = AppTheme.colors.primary
                        )
                    }
                ) {
                    tabs.forEachIndexed { index, title ->
                        Tab(
                            text = {
                                Text(
                                    text = stringResource(title),
                                    style = AppTheme.typography.titleMedium,
                                    color = AppTheme.colors.primary
                                )
                            },
                            selectedContentColor = AppTheme.colors.primary,
                            unselectedContentColor = AppTheme.colors.surface,
                            selected = tabIndex == index,
                            onClick = { tabIndex = index }
                        )
                    }
                }
                when (tabIndex) {
                    0 -> {
                        Spacer(modifier = Modifier.height(Dimens.PADDING_MEDIUM_SMALLER))

                        if (chatDetailsList.isNotEmpty()) {
                            SearchBar(
                                modifier = Modifier
                                    .heightIn(min = Dimens.SEARCH_BAR_MIN_HEIGHT)
                                    .padding(horizontal = Dimens.PADDING_MEDIUM)
                                    .fillMaxWidth(),
                                value = searchValue.value,
                                onSearch = { },
                                onValueChange = { newSearchValue ->
                                    searchValue.value = newSearchValue
                                },
                                onClear = {
                                    searchValue.value = searchValue.value.copy(text = "")
                                }
                            )
                        }

                        Spacer(modifier = Modifier.height(Dimens.PADDING_MEDIUM_SMALLER))
                        ChatConversationsTabSection(
                            modifier = Modifier.weight(1f),
                            chatDetailsList = chatDetailsList,
                            onAnyItemClicked = onAnyChatClicked,
                            searchValue = searchValue.value.text,
                            onNewChatButtonClicked = onNewChatButtonClicked
                        )
                    }

                    1 -> {
                        ChatRequestsTabSection(
                            modifier = Modifier
                                .weight(1f)
                                .padding(top = Dimens.PADDING_MEDIUM),
                            requestedUsers = requestedUsers,
                            receivedRequests = receivedRequests,
                            onRemoveUserRequestClicked = onRemoveUserRequestClicked,
                            onAcceptRequest = onAcceptRequest,
                            onRejectRequest = onRejectRequest,
                            onAnyFriendClicked = onAnyFriendClicked,
                            friendsList = friendsList,
                            onAnyDetailsClicked = onAnyDetailsClicked
                        )
                    }
                }
            }
        }
        FloatingActionButton(
            modifier = Modifier
                .padding(all = Dimens.PADDING_MEDIUM)
                .align(Alignment.BottomEnd),
            containerColor = AppTheme.colors.primary,
            contentColor = White,
            onClick = onNewChatButtonClicked
        ) {
            Icon(
                modifier = Modifier.size(Dimens.ADD_CHAT_BUTTON_ICON_SIZE),
                painter = painterResource(R.drawable.ic_plus),
                contentDescription = null
            )
        }
    }
}

@LightAndDarkPreview
@Composable
fun ChatContentSectionPreview() {
    AppTheme {
        ChatContentSection(
            modifier = Modifier
                .fillMaxSize()
                .background(color = AppTheme.colors.surface),
            chatDetailsList = listOf(
                ChatDetails(
                    chatName = "Gorbachev",
                    lastMessage = "Hello"
                ),
                ChatDetails(
                    chatName = "Brezhnev",
                    lastMessage = "Hello"
                )
            ),
            requestedUsers = listOf(
                UserDetails(
                    username = "Oleg Ivanov",
                    email = "oleg@gmail.com"
                ),
                UserDetails(
                    username = "Dima Korolev",
                    email = "dime@gmail.com"
                )
            ),
            onNewChatButtonClicked = { },
            onAnyChatClicked = { },
            onRemoveUserRequestClicked = { },
            onRejectRequest = { },
            onAcceptRequest = { },
            friendsList = listOf(
                UserDetails(
                    username = "Kovolev Oleg",
                    email = "oolllleeg@gmail.com"
                ),
                UserDetails(
                    username = "Konev Vasya",
                    email = "vasya@gmail.com"
                )
            ),
            onAnyFriendClicked = { },
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
            onAnyDetailsClicked = { }
        )
    }
}

@LightAndDarkPreview
@Composable
fun ChatListItemPreview() {
    AppTheme {
        ChatListItem(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = AppTheme.colors.surface),
            chatDetails = ChatDetails(
                chatId = "0",
                chatName = "MyFavChat",
                chatImageUrl = "",
                lastMessage = "Last message"
            )
        )
    }
}