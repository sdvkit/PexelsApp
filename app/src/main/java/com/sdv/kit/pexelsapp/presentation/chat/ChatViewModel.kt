package com.sdv.kit.pexelsapp.presentation.chat

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.snapshots
import com.google.firebase.ktx.Firebase
import com.sdv.kit.pexelsapp.domain.manager.GoogleAuthManager
import com.sdv.kit.pexelsapp.domain.model.ChatDetails
import com.sdv.kit.pexelsapp.domain.model.UserDetails
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val googleAuthManager: GoogleAuthManager
) : ViewModel() {

    private val _chats = mutableStateOf<List<ChatDetails>>(emptyList())
    val chats: State<List<ChatDetails>> = _chats

    private val _friends = mutableStateOf<List<UserDetails>>(emptyList())
    val friends: State<List<UserDetails>> = _friends

    private val _usersSearch = mutableStateOf(UsersSearchState())
    val usersSearch: State<UsersSearchState> = _usersSearch

    private val _user = mutableStateOf<UserDetails?>(null)
    val user: State<UserDetails?> = _user

    private val _requestedUsers = mutableStateOf<List<UserDetails>>(emptyList())
    val requestedUsers: State<List<UserDetails>> = _requestedUsers

    private val _receivedRequests = mutableStateOf<List<UserDetails>>(emptyList())
    val receivedRequests: State<List<UserDetails>> = _receivedRequests

    private val firebaseDb = Firebase.firestore

    init {
        getUser()
        getFriends()
        getChats()
        getRequestedUsers()
        getReceivedRequests()
    }

    fun deleteFriend(userDetails: UserDetails) {
        viewModelScope.launch(Dispatchers.IO) {
            firebaseDb.collection("users/${userDetails.userId}/friends")
                .document(_user.value?.userId ?: return@launch)
                .delete()

            firebaseDb.collection("users/${_user.value?.userId}/friends")
                .document(userDetails.userId)
                .delete()
        }
    }

    fun acceptRequest(userDetails: UserDetails) {
        viewModelScope.launch(Dispatchers.IO) {
            firebaseDb.collection("users/${userDetails.userId}/friends")
                .document(_user.value?.userId ?: return@launch)
                .set(_user.value ?: return@launch)

            firebaseDb.collection("users/${_user.value?.userId}/friends")
                .document(userDetails.userId)
                .set(userDetails)

            viewModelScope.launch(Dispatchers.IO) {
                firebaseDb.collection("users/${userDetails.userId}/sentRequests")
                    .document(_user.value?.userId ?: "")
                    .delete()

                firebaseDb.collection("users/${_user.value?.userId}/receivedRequests")
                    .document(userDetails.userId)
                    .delete()
            }
        }
    }

    fun rejectRequest(userDetails: UserDetails) {
        viewModelScope.launch(Dispatchers.IO) {
            firebaseDb.collection("users/${userDetails.userId}/sentRequests")
                .document(_user.value?.userId ?: return@launch)
                .delete()

            firebaseDb.collection("users/${_user.value?.userId}/receivedRequests")
                .document(userDetails.userId)
                .delete()
        }
    }

    fun sendAddFriendRequest(userDetails: UserDetails) {
        viewModelScope.launch(Dispatchers.IO) {
            firebaseDb.collection("users/${userDetails.userId}/receivedRequests")
                .document(_user.value?.userId ?: return@launch)
                .set(_user.value ?: return@launch)

            firebaseDb.collection("users/${_user.value?.userId}/sentRequests")
                .document(userDetails.userId)
                .set(userDetails)
        }
    }

    fun cancelAddFriendRequest(userDetails: UserDetails) {
        viewModelScope.launch(Dispatchers.IO) {
            firebaseDb.collection("users/${userDetails.userId}/receivedRequests")
                .document(_user.value?.userId ?: return@launch)
                .delete()

            firebaseDb.collection("users/${_user.value?.userId}/sentRequests")
                .document(userDetails.userId)
                .delete()
        }
    }

    fun searchUsers(query: String) {
        if (query.isBlank()) {
            _usersSearch.value = _usersSearch.value.copy(
                isSearching = false,
                searchResult = emptyList()
            )
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            _usersSearch.value = _usersSearch.value.copy(isSearching = true)

            val user = googleAuthManager.getSignedInUser() ?: return@launch

            firebaseDb.collection("users")
                .get()
                .addOnSuccessListener { result ->
                    _usersSearch.value = _usersSearch.value.copy(
                        searchResult = result
                            .map { userDocument ->
                                userDocument.toObject(UserDetails::class.java)
                            }
                            .filter { userDetails ->
                                userDetails.username?.lowercase()
                                    ?.contains(query.lowercase()) == true
                                        || userDetails.email.lowercase().contains(query.lowercase())
                            }
                            .filter { userDetails -> userDetails.userId != user.userId }
                            .filter { userDetails ->
                                val chat =
                                    _chats.value.find { chatDetails -> chatDetails.chatName == userDetails.username }
                                chat == null
                            }
                    )

                    _usersSearch.value = _usersSearch.value.copy(isSearching = false)
                }
        }
    }

    private fun getReceivedRequests() {
        viewModelScope.launch(Dispatchers.IO) {
            val currentUser = googleAuthManager.getSignedInUser() ?: return@launch

            val requests = firebaseDb.collection("users/${currentUser.userId}/receivedRequests")
                .snapshots()
                .map { snapshot ->
                    snapshot.toObjects(UserDetails::class.java)
                }

            withContext(Dispatchers.Main) {
                requests.collect { users ->
                    _receivedRequests.value = users
                }
            }
        }
    }

    private fun getRequestedUsers() {
        viewModelScope.launch(Dispatchers.IO) {
            val currentUser = googleAuthManager.getSignedInUser() ?: return@launch

            val requests = firebaseDb.collection("users/${currentUser.userId}/sentRequests")
                .snapshots()
                .map { snapshot ->
                    snapshot.toObjects(UserDetails::class.java)
                }

            withContext(Dispatchers.Main) {
                requests.collect { users ->
                    _requestedUsers.value = users
                }
            }
        }
    }

    private fun getUser() {
        viewModelScope.launch(Dispatchers.IO) {
            val currentUser = googleAuthManager.getSignedInUser() ?: return@launch

            withContext(Dispatchers.Main) {
                _user.value = currentUser
            }
        }
    }

    private fun getChats() {
        viewModelScope.launch(Dispatchers.IO) {
            val currentUser = googleAuthManager.getSignedInUser() ?: return@launch

            val chatDetailsFlow =
                firebaseDb.collection("users/${currentUser.userId}/chats")
                    .snapshots()
                    .map { snapshot ->
                        snapshot.toObjects(ChatDetails::class.java)
                    }

            withContext(Dispatchers.Main) {
                chatDetailsFlow.collect { chats ->
                    _chats.value = chats
                }
            }
        }
    }

    private fun getFriends() {
        viewModelScope.launch(Dispatchers.IO) {
            val currentUser = googleAuthManager.getSignedInUser() ?: return@launch

            val chatDetailsFlow =
                firebaseDb.collection("users/${currentUser.userId}/friends")
                    .snapshots()
                    .map { snapshot ->
                        snapshot.toObjects(UserDetails::class.java)
                    }

            withContext(Dispatchers.Main) {
                chatDetailsFlow.collect { friends ->
                    _friends.value = friends
                }
            }
        }
    }
}