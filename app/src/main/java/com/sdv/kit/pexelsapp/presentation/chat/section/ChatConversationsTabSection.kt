package com.sdv.kit.pexelsapp.presentation.chat.section

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.sdv.kit.pexelsapp.domain.model.ChatDetails
import com.sdv.kit.pexelsapp.presentation.Dimens
import com.sdv.kit.pexelsapp.presentation.common.item.ChatListItem
import com.sdv.kit.pexelsapp.presentation.common.stub.NoChatsStub
import com.sdv.kit.pexelsapp.presentation.common.stub.NoSearchResultsStub

@Composable
fun ChatConversationsTabSection(
    modifier: Modifier = Modifier,
    chatDetailsList: List<ChatDetails>,
    onAnyItemClicked: (ChatDetails) -> Unit,
    searchValue: String,
    onNewChatButtonClicked: () -> Unit
) {
    val filteredChats = filterChats(
        chats = chatDetailsList,
        query = searchValue
    )

    when (chatDetailsList.isNotEmpty()) {
        true -> {
            when (filteredChats.isNotEmpty()) {
                true -> {
                    LazyColumn(modifier = modifier) {
                        items(filteredChats) { chatDetails ->
                            ChatListItem(
                                modifier = Modifier
                                    .clickable {
                                        onAnyItemClicked(chatDetails)
                                    }
                                    .fillMaxWidth()
                                    .padding(
                                        horizontal = Dimens.PADDING_MEDIUM,
                                        vertical = Dimens.PADDING_SMALL
                                    ),
                                chatDetails = chatDetails
                            )
                        }
                    }
                }

                false -> {
                    NoSearchResultsStub(modifier = Modifier.fillMaxSize())
                }
            }
        }

        false -> {
            NoChatsStub(
                modifier = Modifier.fillMaxSize(),
                onClick = onNewChatButtonClicked
            )
        }
    }
}

private fun filterChats(chats: List<ChatDetails>, query: String): List<ChatDetails> {
    return chats.filter { chatDetails ->
        chatDetails.chatName.lowercase().contains(query.lowercase())
                || chatDetails.lastMessage.lowercase().contains(query.lowercase())
    }
}