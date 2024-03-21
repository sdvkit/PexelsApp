package com.sdv.kit.pexelsapp.presentation.chat.conversation

import android.graphics.Bitmap
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.snapshots
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.sdv.kit.pexelsapp.data.remote.dto.Notification
import com.sdv.kit.pexelsapp.data.remote.dto.NotificationRequest
import com.sdv.kit.pexelsapp.domain.manager.FCMManager
import com.sdv.kit.pexelsapp.domain.manager.GoogleAuthManager
import com.sdv.kit.pexelsapp.domain.model.Bookmarked
import com.sdv.kit.pexelsapp.domain.model.Chat
import com.sdv.kit.pexelsapp.domain.model.ChatDetails
import com.sdv.kit.pexelsapp.domain.model.Message
import com.sdv.kit.pexelsapp.domain.model.MessageType
import com.sdv.kit.pexelsapp.domain.model.UserDetails
import com.sdv.kit.pexelsapp.domain.usecase.bookmarked.GetBookmarkedPhotos
import com.sdv.kit.pexelsapp.domain.usecase.fcm.SendNotification
import com.sdv.kit.pexelsapp.domain.usecase.photo.GetLocalImagesByPrefix
import com.sdv.kit.pexelsapp.util.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ConversationViewModel @Inject constructor(
    private val googleAuthManager: GoogleAuthManager,
    private val sendNotificationUsecase: SendNotification,
    private val fcmManager: FCMManager,
    private val getBookmarkedPhotosUsecase: GetBookmarkedPhotos,
    private val getLocalImagesByPrefixUsecase: GetLocalImagesByPrefix
) : ViewModel() {

    private val firebaseDb = Firebase.firestore

    private val _user = mutableStateOf<UserDetails?>(null)
    val user: State<UserDetails?> = _user

    private val _chat = mutableStateOf<Chat?>(null)
    val chat: State<Chat?> = _chat

    private val _clickedChatDetails = mutableStateOf<ChatDetails?>(null)
    val clickedChatDetails: State<ChatDetails?> = _clickedChatDetails

    private val _clickedUserDetails = mutableStateOf<UserDetails?>(null)
    val clickedUserDetails: State<UserDetails?> = _clickedUserDetails

    private val _bookmarkedPhotos = mutableStateOf<Flow<PagingData<Bookmarked>>?>(null)
    val bookmarkedPhotos: State<Flow<PagingData<Bookmarked>>?> = _bookmarkedPhotos

    private val _localImages = mutableStateOf<List<Bitmap>>(emptyList())
    val localImages: State<List<Bitmap>> = _localImages

    init {
        viewModelScope.launch {
            _user.value = googleAuthManager.getSignedInUser()
        }
    }

    fun getLocalImages() {
        viewModelScope.launch(Dispatchers.IO) {
            val localImages = getLocalImagesByPrefixUsecase(prefix = Constants.IMAGE_NAME_PREFIX)
            _localImages.value = localImages
        }
    }

    fun getBookmarkedPhotos() {
        viewModelScope.launch(Dispatchers.IO) {
            val bookmarkedPhotos = getBookmarkedPhotosUsecase()
                .cachedIn(viewModelScope)

            _bookmarkedPhotos.value = bookmarkedPhotos
        }
    }

    fun newChat(
        friendUserDetails: UserDetails,
        message: String,
        messageType: MessageType
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val newChatDocument = firebaseDb.collection("chats").document(friendUserDetails.userId)
            val user = googleAuthManager.getSignedInUser() ?: return@launch
            val lastMessage = if (messageType == MessageType.TEXT) message else "image.jpg"

            val senderChatDetails = ChatDetails(
                chatId = newChatDocument.id,
                chatName = friendUserDetails.username ?: "",
                chatImageUrl = friendUserDetails.profilePictureUrl ?: "",
                lastMessage = lastMessage
            )

            val receiverChatDetails = ChatDetails(
                chatId = newChatDocument.id,
                chatName = user.username ?: "",
                chatImageUrl = user.profilePictureUrl ?: "",
                lastMessage = lastMessage
            )

            firebaseDb.collection("users/${user.userId}/chats")
                .document(newChatDocument.id)
                .set(senderChatDetails)

            firebaseDb.collection("users/${friendUserDetails.userId}/chats")
                .document(newChatDocument.id)
                .set(receiverChatDetails)

            val creatableChatWithUsers = Chat(
                chatDetails = senderChatDetails,
                messages = listOf(
                    Message(
                        from = user,
                        text = message,
                        messageType = messageType
                    )
                ),
                users = listOf(user, friendUserDetails)
            )

            newChatDocument.set(creatableChatWithUsers)
            getChat(chatDetails = senderChatDetails)
        }
    }

    fun sendMessage(message: String, messageType: MessageType) {
        viewModelScope.launch(Dispatchers.IO) {
            val chatId = _chat.value?.chatDetails?.chatId ?: ""

            val sentMessage = Message(
                from = _user.value,
                text = message,
                messageType = messageType
            )

            firebaseDb.collection("chats")
                .document(chatId)
                .set(
                    _chat.value?.copy(
                        messages = (_chat.value?.messages ?: emptyList()) + sentMessage
                    ) ?: return@launch
                )

            val currentFcmToken = fcmManager.readToken()

            _chat.value?.users?.forEach { userDetails ->
                if (currentFcmToken != userDetails.fcmToken) {
                    sendNotificationUsecase(
                        notificationRequest = NotificationRequest(
                            targetFcmToken = userDetails.fcmToken,
                            notification = Notification(
                                title = sentMessage.from?.username ?: "",
                                body = sentMessage.text
                            )
                        )
                    )
                }

                firebaseDb.collection("users/${userDetails.userId}/chats")
                    .document(chatId)
                    .get()
                    .addOnSuccessListener { chatDetailsDocument ->
                        val oldChatDetails = chatDetailsDocument.toObject(ChatDetails::class.java)
                        val lastMessage =
                            if (messageType == MessageType.TEXT) sentMessage.text else "image.jpg"

                        firebaseDb.collection("users/${userDetails.userId}/chats")
                            .document(chatId)
                            .set(
                                oldChatDetails?.copy(
                                    lastMessage = lastMessage
                                ) ?: return@addOnSuccessListener
                            )
                    }
            }
        }
    }

    fun observeMessages() {
        viewModelScope.launch(Dispatchers.IO) {
            val chatId = _chat.value?.chatDetails?.chatId ?: return@launch

            val chatDetailsFlow =
                firebaseDb.collection("chats")
                    .document(chatId)
                    .snapshots()
                    .map { snapshot ->
                        snapshot.toObject(Chat::class.java)
                    }

            withContext(Dispatchers.Main) {
                chatDetailsFlow.collect { chat ->
                    _chat.value = chat
                }
            }
        }
    }

    fun getChat(chatDetails: ChatDetails) {
        viewModelScope.launch(Dispatchers.IO) {
            firebaseDb.collection("chats")
                .document(chatDetails.chatId)
                .get()
                .addOnSuccessListener { chatDocument ->
                    _chat.value = chatDocument.toObject(Chat::class.java)
                }
        }
    }

    fun tryGetOldChat(serializedUserDetails: String?) {
        viewModelScope.launch(Dispatchers.IO) {
            serializedUserDetails ?: return@launch
            val userDetails = Gson().fromJson(serializedUserDetails, UserDetails::class.java)

            firebaseDb.collection("chats")
                .document(userDetails.userId)
                .get()
                .addOnSuccessListener { chatDocument ->
                    _chat.value = chatDocument.toObject(Chat::class.java)
                }
        }
    }

    fun deleteChat() {
        viewModelScope.launch(Dispatchers.IO) {
            val chatId = _chat.value?.chatDetails?.chatId ?: return@launch

            _chat.value?.users?.forEach { userDetails ->
                firebaseDb.collection("users/${userDetails.userId}/chats")
                    .document(chatId)
                    .delete()
            }

            firebaseDb.collection("chats")
                .document(chatId)
                .delete()
        }
    }

    fun deserializeUserDetails(serializedUserDetails: String?) {
        viewModelScope.launch(Dispatchers.Main) {
            serializedUserDetails ?: return@launch

            _clickedUserDetails.value =
                Gson().fromJson(serializedUserDetails, UserDetails::class.java)
        }
    }

    fun deserializeChatDetails(serializedChatDetails: String?) {
        viewModelScope.launch(Dispatchers.Main) {
            serializedChatDetails ?: return@launch

            _clickedChatDetails.value =
                Gson().fromJson(serializedChatDetails, ChatDetails::class.java)
        }
    }
}