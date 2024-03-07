package com.sdv.kit.pexelsapp.presentation.chat

import com.sdv.kit.pexelsapp.domain.model.UserDetails

data class UsersSearchState(
    val isSearching: Boolean = false,
    val searchResult: List<UserDetails> = emptyList()
)