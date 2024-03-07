package com.sdv.kit.pexelsapp.domain.model

data class Message(
    var from: UserDetails? = null,
    var text: String = "",
    var messageType: MessageType = MessageType.TEXT
)

enum class MessageType {
    TEXT,
    LOCAL_PHOTO,
    REMOTE_PHOTO
}