package com.sdv.kit.pexelsapp.presentation.common.sheet

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import com.sdv.kit.pexelsapp.R
import com.sdv.kit.pexelsapp.domain.model.UserDetails
import com.sdv.kit.pexelsapp.presentation.Dimens
import com.sdv.kit.pexelsapp.presentation.annotation.LightAndDarkPreview
import com.sdv.kit.pexelsapp.presentation.chat.UsersSearchState
import com.sdv.kit.pexelsapp.presentation.common.AnimatedLottieImage
import com.sdv.kit.pexelsapp.presentation.common.SearchBar
import com.sdv.kit.pexelsapp.presentation.common.item.UserDetailsListItem
import com.sdv.kit.pexelsapp.presentation.common.stub.NoSearchResultsStub
import com.sdv.kit.pexelsapp.presentation.ui.theme.AppTheme
import com.sdv.kit.pexelsapp.util.Constants
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun NewChatBottomSheetContent(
    modifier: Modifier = Modifier,
    currentUser: UserDetails,
    searchState: UsersSearchState,
    onSearch: (String) -> Unit,
    onAddUserClicked: (UserDetails) -> Unit,
    requestedUsers: List<UserDetails>,
    onRemoveUserRequestClicked: (UserDetails) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val searchValue = remember { mutableStateOf(TextFieldValue(text = "")) }
    val searchJob = remember { mutableStateOf<Job?>(null) }

    Column(modifier = modifier) {
        Spacer(modifier = Modifier.height(Dimens.PADDING_MEDIUM_SMALLER))
        SearchBar(
            modifier = Modifier
                .heightIn(min = Dimens.SEARCH_BAR_MIN_HEIGHT)
                .fillMaxWidth(),
            placeholderText = R.string.search_user,
            value = searchValue.value,
            onSearch = { searchQuery ->
                onSearch(searchQuery.text)
            },
            onValueChange = { newSearchValue ->
                if (newSearchValue.text.isBlank()) {
                    onSearch(newSearchValue.text)
                }

                searchJob.value?.cancel()
                searchJob.value = coroutineScope.launch {
                    delay(Constants.USER_FRIENDLY_SEARCH_INTERVAL)
                    onSearch(newSearchValue.text)
                }

                searchValue.value = newSearchValue
            },
            onClear = {
                searchValue.value = searchValue.value.copy(text = "")
                onSearch(searchValue.value.text)
            }
        )
        Spacer(modifier = Modifier.height(Dimens.PADDING_MEDIUM_SMALLER))
        if (searchState.isSearching) {
            Spacer(modifier = Modifier.height(Dimens.PADDING_SMALL))
            LinearProgressIndicator(
                modifier = Modifier
                    .padding(end = Dimens.PADDING_SMALL)
                    .clip(CircleShape)
                    .fillMaxWidth()
                    .height(Dimens.LOADING_PROGRESS_BAR_HEIGHT),
                color = AppTheme.colors.progressBackground,
                trackColor = AppTheme.colors.primary
            )
        }
        Spacer(modifier = Modifier.height(Dimens.PADDING_MEDIUM_SMALLER))

        when {
            searchState.searchResult.isNotEmpty() && !searchState.isSearching -> {
                LazyColumn {
                    items(searchState.searchResult) { userDetails ->
                        UserDetailsListItem(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = Dimens.PADDING_SMALL),
                            userDetails = userDetails,
                            onAddUserClicked = onAddUserClicked,
                            isUserInFriends = userDetails in currentUser.friends,
                            requestedUsers = requestedUsers,
                            onRemoveUserRequestClicked = onRemoveUserRequestClicked
                        )
                    }
                }
            }

            searchState.searchResult.isEmpty()
                    && !searchState.isSearching
                    && searchJob.value?.isCompleted == true
                    && searchValue.value.text.isNotEmpty() -> {
                Spacer(modifier = Modifier.weight(1f))
                NoSearchResultsStub(
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                Spacer(modifier = Modifier.weight(1f))
            }

            else -> {
                Spacer(modifier = Modifier.weight(1f))
                Column(modifier = Modifier.align(Alignment.CenterHorizontally)) {
                    AnimatedLottieImage(
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .size(Dimens.STUB_ANIMATED_IMAGE_SIZE),
                        res = R.raw.image_search_anim
                    )
                    Text(
                        text = stringResource(R.string.search_users_stub),
                        color = AppTheme.colors.textColorVariant,
                        style = AppTheme.typography.labelMedium,
                        textAlign = TextAlign.Center
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
            }
        }

        Spacer(modifier = Modifier.height(Dimens.PADDING_BIG))
    }
}

@LightAndDarkPreview
@Composable
fun NewChatBottomSheetContentPreview() {
    AppTheme {
        Surface {
            NewChatBottomSheetContent(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = Dimens.PADDING_MEDIUM),
                searchState = UsersSearchState(),
                onSearch = { },
                onAddUserClicked = { },
                currentUser = UserDetails(),
                requestedUsers = emptyList(),
                onRemoveUserRequestClicked = { }
            )
        }
    }
}