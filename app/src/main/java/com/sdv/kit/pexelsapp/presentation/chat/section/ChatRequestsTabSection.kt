package com.sdv.kit.pexelsapp.presentation.chat.section

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import com.sdv.kit.pexelsapp.R
import com.sdv.kit.pexelsapp.domain.model.UserDetails
import com.sdv.kit.pexelsapp.presentation.Dimens
import com.sdv.kit.pexelsapp.presentation.common.SearchBar
import com.sdv.kit.pexelsapp.presentation.common.item.FriendListItem
import com.sdv.kit.pexelsapp.presentation.common.item.UserDetailsListItem
import com.sdv.kit.pexelsapp.presentation.common.stub.NoFriendsStub
import com.sdv.kit.pexelsapp.presentation.common.stub.NoSearchResultsStub
import com.sdv.kit.pexelsapp.presentation.ui.theme.AppTheme

@Composable
fun ChatRequestsTabSection(
    modifier: Modifier = Modifier,
    friendsList: List<UserDetails>,
    requestedUsers: List<UserDetails>,
    receivedRequests: List<UserDetails>,
    onRejectRequest: (UserDetails) -> Unit,
    onAcceptRequest: (UserDetails) -> Unit,
    onRemoveUserRequestClicked: (UserDetails) -> Unit,
    onAnyFriendClicked: (UserDetails) -> Unit,
    onAnyDetailsClicked: (UserDetails) -> Unit
) {
    val searchValue = remember { mutableStateOf(TextFieldValue(text = "")) }

    Column(modifier = modifier) {

        if (friendsList.isNotEmpty() || requestedUsers.isNotEmpty() || receivedRequests.isNotEmpty()) {
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

        when (isSearchResultsPresent(friendsList, requestedUsers, receivedRequests, searchValue)) {
            true -> {
                Spacer(modifier = Modifier.weight(1f))
                NoSearchResultsStub(modifier = Modifier.align(Alignment.CenterHorizontally))
                Spacer(modifier = Modifier.weight(1f))
            }

            false -> {
                LazyColumn(modifier = Modifier.weight(1f)) {
                    requestedUsersSectionContent(
                        scope = this,
                        requestedUsers = requestedUsers,
                        onRemoveUserRequestClicked = onRemoveUserRequestClicked,
                        searchValue = searchValue
                    )

                    receivedRequestsSectionContent(
                        scope = this,
                        receivedRequests = receivedRequests,
                        onRejectRequest = onRejectRequest,
                        onAcceptRequest = onAcceptRequest,
                        searchValue = searchValue
                    )

                    friendsSectionContent(
                        scope = this,
                        friendsList = friendsList,
                        onAnyFriendClicked = onAnyFriendClicked,
                        onAnyDetailsClicked = onAnyDetailsClicked,
                        searchValue = searchValue
                    )
                }
            }
        }
    }

    if (receivedRequests.isEmpty() && requestedUsers.isEmpty() && friendsList.isEmpty()) {
        NoFriendsStub(modifier = Modifier.fillMaxSize())
    }
}

private fun friendsSectionContent(
    scope: LazyListScope,
    friendsList: List<UserDetails>,
    onAnyFriendClicked: (UserDetails) -> Unit,
    onAnyDetailsClicked: (UserDetails) -> Unit,
    searchValue: MutableState<TextFieldValue>
) {
    if (
        filterUsersList(
            users = friendsList,
            query = searchValue.value.text
        ).isNotEmpty()
    ) {
        scope.item {
            Text(
                modifier = Modifier.padding(horizontal = Dimens.PADDING_MEDIUM),
                text = stringResource(R.string.friends),
                style = AppTheme.typography.titleMedium,
                color = AppTheme.colors.textColor
            )
        }

        scope.items(
            filterUsersList(
                users = friendsList,
                query = searchValue.value.text
            )
        ) { userDetails ->
            FriendListItem(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = Dimens.PADDING_MEDIUM,
                        vertical = Dimens.PADDING_SMALL
                    )
                    .clickable {
                        onAnyFriendClicked(userDetails)
                    },
                userDetails = userDetails,
                onDetailsClicked = onAnyDetailsClicked
            )
        }
    }
}

private fun receivedRequestsSectionContent(
    scope: LazyListScope,
    receivedRequests: List<UserDetails>,
    onRejectRequest: (UserDetails) -> Unit,
    onAcceptRequest: (UserDetails) -> Unit,
    searchValue: MutableState<TextFieldValue>
) {
    if (
        filterUsersList(
            users = receivedRequests,
            query = searchValue.value.text
        ).isNotEmpty()
    ) {
        scope.item {
            Text(
                modifier = Modifier.padding(horizontal = Dimens.PADDING_MEDIUM),
                text = stringResource(R.string.received_requests),
                style = AppTheme.typography.titleMedium,
                color = AppTheme.colors.textColor
            )
        }

        scope.items(
            filterUsersList(
                users = receivedRequests,
                query = searchValue.value.text
            )
        ) { receivedRequestUserDetails ->
            UserDetailsListItem(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        horizontal = Dimens.PADDING_MEDIUM,
                        vertical = Dimens.PADDING_SMALL
                    ),
                userDetails = receivedRequestUserDetails,
                isUserInFriends = false,
                onAddUserClicked = { userDetails ->
                    onAcceptRequest(userDetails)
                },
                onReject = { userDetails ->
                    onRejectRequest(userDetails)
                },
                requestedUsers = emptyList(),
                onRemoveUserRequestClicked = { }
            )
        }
    }
}

private fun requestedUsersSectionContent(
    scope: LazyListScope,
    requestedUsers: List<UserDetails>,
    onRemoveUserRequestClicked: (UserDetails) -> Unit,
    searchValue: MutableState<TextFieldValue>
) {
    if (
        filterUsersList(
            users = requestedUsers,
            query = searchValue.value.text
        ).isNotEmpty()
    ) {
        scope.item {
            Text(
                modifier = Modifier.padding(horizontal = Dimens.PADDING_MEDIUM),
                text = stringResource(R.string.sent_requests),
                style = AppTheme.typography.titleMedium,
                color = AppTheme.colors.textColor
            )
        }

        scope.items(
            filterUsersList(
                users = requestedUsers,
                query = searchValue.value.text
            )
        ) { requestedUser ->
            UserDetailsListItem(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        horizontal = Dimens.PADDING_MEDIUM,
                        vertical = Dimens.PADDING_SMALL
                    ),
                userDetails = requestedUser,
                isUserInFriends = false,
                onAddUserClicked = { },
                requestedUsers = listOf(requestedUser),
                onRemoveUserRequestClicked = onRemoveUserRequestClicked
            )
        }
    }
}

private fun isSearchResultsPresent(
    friendsList: List<UserDetails>,
    requestedUsers: List<UserDetails>,
    receivedRequests: List<UserDetails>,
    searchValue: MutableState<TextFieldValue>
): Boolean {
    return filterUsersList(
        users = receivedRequests,
        query = searchValue.value.text
    ).isEmpty()
            && filterUsersList(
        users = requestedUsers,
        query = searchValue.value.text
    ).isEmpty()
            && filterUsersList(
        users = friendsList,
        query = searchValue.value.text
    ).isEmpty()
}

private fun filterUsersList(users: List<UserDetails>, query: String): List<UserDetails> {
    return users.filter { userDetails ->
        userDetails.username?.lowercase()?.contains(query.lowercase()) == true
                || userDetails.email.lowercase().contains(query.lowercase())
    }
}