package com.sdv.kit.pexelsapp.domain.model

data class ChatDetails(
    var chatId: String = "",
    var chatName: String = "",
    var chatImageUrl: String = "",
    var lastMessage: String = ""
)

data class Chat(
    var chatDetails: ChatDetails? = null,
    var users: List<UserDetails> = emptyList(),
    val messages: List<Message> = emptyList()
)