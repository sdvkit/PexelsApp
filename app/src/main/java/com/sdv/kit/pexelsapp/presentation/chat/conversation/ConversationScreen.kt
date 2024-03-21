package com.sdv.kit.pexelsapp.presentation.chat.conversation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.sdv.kit.pexelsapp.domain.model.Chat
import com.sdv.kit.pexelsapp.domain.model.ChatDetails
import com.sdv.kit.pexelsapp.domain.model.Message
import com.sdv.kit.pexelsapp.domain.model.UserDetails
import com.sdv.kit.pexelsapp.presentation.Dimens
import com.sdv.kit.pexelsapp.presentation.annotation.LightAndDarkPreview
import com.sdv.kit.pexelsapp.presentation.chat.conversation.section.ConversationInputSection
import com.sdv.kit.pexelsapp.presentation.chat.conversation.section.ConversationTopBarSection
import com.sdv.kit.pexelsapp.presentation.common.item.MessageItem
import com.sdv.kit.pexelsapp.presentation.common.stub.NoMessagesStub
import com.sdv.kit.pexelsapp.presentation.ui.theme.AppTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun ConversationScreen(
    modifier: Modifier = Modifier,
    onBackButtonClicked: () -> Unit,
    onDetailsButtonClicked: () -> Unit,
    user: UserDetails,
    clickedUserDetails: UserDetails? = null,
    chat: Chat? = null,
    clickedChatDetails: ChatDetails? = null,
    onAttachButtonClicked: () -> Unit,
    onSendButtonClicked: (String) -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()
    val lazyListState = rememberLazyListState()

    val header =
        clickedChatDetails?.chatName ?: chat?.chatDetails?.chatName ?: clickedUserDetails?.username
        ?: ""
    val imageUrl = clickedChatDetails?.chatImageUrl ?: chat?.chatDetails?.chatImageUrl
    ?: clickedUserDetails?.profilePictureUrl ?: ""

    Column(modifier = modifier) {
        ConversationTopBarSection(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = Dimens.PADDING_MEDIUM_SMALLER),
            header = header,
            imageUrl = imageUrl,
            onBackButtonClicked = onBackButtonClicked,
            onDetailsButtonClicked = onDetailsButtonClicked
        )
        Spacer(modifier = Modifier.height(Dimens.PADDING_MEDIUM))
        ConversationBody(
            modifier = Modifier.weight(1f),
            user = user,
            chat = chat,
            coroutineScope = coroutineScope,
            lazyListState = lazyListState
        )
        Spacer(modifier = Modifier.height(Dimens.PADDING_MEDIUM))
        ConversationInputSection(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Dimens.PADDING_MEDIUM_SMALLER),
            onSendButtonClicked = { message ->
                onSendButtonClicked(message)

                coroutineScope.launch {
                    lazyListState.animateScrollToItem(chat?.messages?.lastIndex ?: 0)
                }
            },
            onAttachButtonClicked = onAttachButtonClicked
        )
        Spacer(modifier = Modifier.height(Dimens.PADDING_MEDIUM))
    }
}

@Composable
private fun ConversationBody(
    modifier: Modifier = Modifier,
    user: UserDetails,
    chat: Chat? = null,
    coroutineScope: CoroutineScope,
    lazyListState: LazyListState
) {
    Column(modifier = modifier) {
        when (chat != null) {
            true -> {
                LaunchedEffect(Unit) {
                    coroutineScope.launch {
                        lazyListState.animateScrollToItem(chat.messages.lastIndex)
                    }
                }

                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = Dimens.PADDING_MEDIUM_SMALLER),
                    state = lazyListState
                ) {
                    items(chat.messages) { message ->
                        MessageItem(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = Dimens.PADDING_SMALL),
                            message = message,
                            user = user
                        )
                    }
                    item {
                        Spacer(modifier = Modifier.height(Dimens.PADDING_EXTRA_BIG))
                    }
                }
            }

            false -> NoMessagesStub(modifier = Modifier.fillMaxWidth())
        }
    }
}

@LightAndDarkPreview
@Composable
fun ConversationScreenPreview() {
    AppTheme {
        val userIvan = UserDetails(username = "Ivanov Ivan", email = "ivan@mail.ru")
        val userNikita = UserDetails(username = "Mikola Delopata", email = "sudaevnikita@mail.ru")

        ConversationScreen(
            modifier = Modifier
                .fillMaxSize()
                .background(color = AppTheme.colors.surface),
            onBackButtonClicked = { },
            onDetailsButtonClicked = { },
            onSendButtonClicked = { },
            onAttachButtonClicked = { },
            user = userNikita,
            chat = Chat(
                chatDetails = ChatDetails(chatName = userIvan.username ?: ""),
                users = listOf(userIvan, userNikita),
                messages = listOf(
                    Message(
                        from = userIvan,
                        text = "Hello from Ivan"
                    ),
                    Message(
                        from = userNikita,
                        text = "Hello from Mikola"
                    )
                )
            )
        )
    }
}