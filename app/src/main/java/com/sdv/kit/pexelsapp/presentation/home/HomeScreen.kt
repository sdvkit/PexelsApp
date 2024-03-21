package com.sdv.kit.pexelsapp.presentation.home

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import com.sdv.kit.pexelsapp.R
import com.sdv.kit.pexelsapp.domain.model.FeaturedCollection
import com.sdv.kit.pexelsapp.domain.model.Photo
import com.sdv.kit.pexelsapp.domain.model.UserDetails
import com.sdv.kit.pexelsapp.presentation.Dimens
import com.sdv.kit.pexelsapp.presentation.anim.bounceClickEffect
import com.sdv.kit.pexelsapp.presentation.annotation.LightAndDarkPreview
import com.sdv.kit.pexelsapp.presentation.common.SearchBar
import com.sdv.kit.pexelsapp.presentation.common.stub.NoNetworkStub
import com.sdv.kit.pexelsapp.presentation.common.stub.NoResultsStub
import com.sdv.kit.pexelsapp.presentation.home.section.FeaturedSection
import com.sdv.kit.pexelsapp.presentation.home.section.HomePhotoSection
import com.sdv.kit.pexelsapp.presentation.ui.theme.AppTheme
import com.sdv.kit.pexelsapp.util.Constants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.net.UnknownHostException

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    signedInUserDetails: UserDetails,
    onCacheRequest: () -> Boolean,
    isInternetConnected: Boolean,
    isInternetWasDisconnected: MutableState<Boolean>,
    networkStatusToastsCount: MutableIntState,
    collections: LazyPagingItems<FeaturedCollection>,
    photos: LazyPagingItems<Photo>,
    searchQuery: String,
    onSearch: (String) -> Unit,
    selectedFeaturedCollectionIndex: MutableIntState,
    onHttpError: () -> Unit,
    onExplorePhotos: () -> Unit,
    onAnyPhotoClicked: (Photo) -> Unit,
    onCollectionsRequest: () -> Unit,
    onPhotosRequest: () -> Unit,
    onTryAgainRequest: () -> Unit,
    onUserAvatarClicked: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val searchValue = remember { mutableStateOf(TextFieldValue(text = searchQuery)) }
    val searchJob = remember { mutableStateOf<Job?>(null) }

    if (searchValue.value.text.isEmpty()) {
        searchValue.value = searchValue.value.copy(text = searchQuery)
    }

    val isCollectionsLoading = remember { mutableStateOf(false) }
    val isCollectionsError = remember { mutableStateOf(false) }
    val isHttpError = remember { mutableStateOf(false) }
    val isPhotosError = remember { mutableStateOf(false) }
    val isPhotosLoading = remember { mutableStateOf(false) }
    val isPhotoImageLoading = remember { mutableStateOf(false) }

    val shouldShowLoadingProgress = remember { mutableStateOf(false) }
    val shouldShowNoResultsStub = remember { mutableStateOf(false) }
    val shouldShowNetworkStub = remember { mutableStateOf(false) }
    val shouldResendPhotosRequest = remember { mutableStateOf(false) }

    observeCollectionsLoadState(
        collections = collections,
        isCollectionsLoading = isCollectionsLoading,
        isCollectionsError = isCollectionsError
    )

    observePhotosLoadState(
        photos = photos,
        onCacheRequest = onCacheRequest,
        isPhotosLoading = isPhotosLoading,
        isPhotosError = isPhotosError,
        isHttpError = isHttpError,
        shouldShowNetworkStub = shouldShowNetworkStub,
        shouldResendPhotosRequest = shouldResendPhotosRequest
    )

    shouldShowLoadingProgress.value =
        isCollectionsLoading.value || isPhotosLoading.value || isPhotoImageLoading.value
    shouldShowNoResultsStub.value =
        !shouldShowNetworkStub.value && !isPhotosLoading.value && photos.itemCount == 0

    if (networkStatusToastsCount.intValue == 1) {
        var toastMessage = ""

        when (onCacheRequest()) {
            true -> {
                toastMessage = stringResource(R.string.no_connection_data_cached)
            }

            false -> {
                shouldShowNetworkStub.value = true
                toastMessage = stringResource(R.string.no_connection_no_cache)
            }
        }

        Toast.makeText(LocalContext.current, toastMessage, Toast.LENGTH_SHORT).show()
    }

    if (isInternetWasDisconnected.value && isInternetConnected) {
        onCollectionsRequest()

        if (shouldResendPhotosRequest.value && !shouldShowNetworkStub.value) {
            onPhotosRequest()
            shouldResendPhotosRequest.value = false
        }

        shouldShowNetworkStub.value = false
        isInternetWasDisconnected.value = false
        networkStatusToastsCount.intValue = 0
    }

    if (isHttpError.value) {
        onHttpError()
    }

    HomeScreenContent(
        modifier = modifier,
        coroutineScope = coroutineScope,
        signedInUserDetails = signedInUserDetails,
        collections = collections,
        photos = photos,
        onSearch = onSearch,
        searchJob = searchJob,
        searchValue = searchValue,
        selectedFeaturedCollectionIndex = selectedFeaturedCollectionIndex,
        onExplorePhotos = onExplorePhotos,
        onAnyPhotoClicked = onAnyPhotoClicked,
        onTryAgainRequest = onTryAgainRequest,
        shouldShowNetworkStub = shouldShowNetworkStub,
        shouldShowLoadingProgress = shouldShowLoadingProgress,
        shouldShowNoResultsStub = shouldShowNoResultsStub,
        onUserAvatarClicked = onUserAvatarClicked
    )
}

@Composable
private fun HomeScreenContent(
    modifier: Modifier = Modifier,
    coroutineScope: CoroutineScope,
    signedInUserDetails: UserDetails,
    collections: LazyPagingItems<FeaturedCollection>,
    photos: LazyPagingItems<Photo>,
    onSearch: (String) -> Unit,
    searchJob: MutableState<Job?>,
    searchValue: MutableState<TextFieldValue>,
    selectedFeaturedCollectionIndex: MutableIntState,
    onExplorePhotos: () -> Unit,
    onAnyPhotoClicked: (Photo) -> Unit,
    onTryAgainRequest: () -> Unit,
    shouldShowNetworkStub: MutableState<Boolean>,
    shouldShowLoadingProgress: MutableState<Boolean>,
    shouldShowNoResultsStub: MutableState<Boolean>,
    onUserAvatarClicked: () -> Unit
) {
    Column(
        modifier = modifier.background(
            color = AppTheme.colors.surface
        )
    ) {
        Spacer(modifier = Modifier.height(Dimens.PADDING_SMALL))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Dimens.PADDING_MEDIUM),
            verticalAlignment = Alignment.CenterVertically
        ) {
            SearchBar(
                modifier = Modifier
                    .heightIn(min = Dimens.SEARCH_BAR_MIN_HEIGHT)
                    .weight(1f),
                value = searchValue.value,
                onSearch = { searchQuery ->
                    onSearch(searchQuery.text)
                },
                onValueChange = { newSearchValue ->
                    // todo look closer on value change
                    if (newSearchValue.text.isBlank()) {
                        onSearch(newSearchValue.text)
                    }

                    if (selectedFeaturedCollectionIndex.intValue != Constants.EMPTY_COLLECTION_HEADER_INDEX) {
                        val collectionHeader =
                            "${collections[selectedFeaturedCollectionIndex.intValue]!!.title} "

                        if (!newSearchValue.text.contains(collectionHeader)) {
                            selectedFeaturedCollectionIndex.intValue =
                                Constants.EMPTY_COLLECTION_HEADER_INDEX
                        }
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
                    selectedFeaturedCollectionIndex.intValue =
                        Constants.EMPTY_COLLECTION_HEADER_INDEX
                    onSearch(searchValue.value.text)
                }
            )
            Spacer(modifier = Modifier.width(Dimens.PADDING_SMALL))
            AsyncImage(
                modifier = Modifier
                    .bounceClickEffect(0.85f)
                    .size(Dimens.USER_AVATAR_SIZE)
                    .clip(CircleShape)
                    .clickable {
                        onUserAvatarClicked()
                    },
                model = signedInUserDetails.profilePictureUrl ?: "",
                contentDescription = null,
                contentScale = ContentScale.Crop,
                error = painterResource(R.drawable.img_placeholder),
                placeholder = painterResource(R.drawable.img_placeholder)
            )
        }
        Spacer(modifier = Modifier.height(Dimens.PADDING_MEDIUM))

        if (collections.itemCount > 0) {
            FeaturedSection(
                modifier = Modifier.fillMaxWidth(),
                collections = collections,
                selectedItemIndex = selectedFeaturedCollectionIndex.intValue,
                onActiveItemClicked = { featuredCollection ->
                    val newSearchValue =
                        searchValue.value.text.replace("${featuredCollection.title} ", "")
                    searchValue.value = searchValue.value.copy(
                        text = newSearchValue,
                        selection = TextRange(newSearchValue.length)
                    )
                    selectedFeaturedCollectionIndex.intValue =
                        Constants.EMPTY_COLLECTION_HEADER_INDEX
                    onSearch(searchValue.value.text)
                },
                onAnyItemClicked = { index ->
                    if (selectedFeaturedCollectionIndex.intValue != Constants.EMPTY_COLLECTION_HEADER_INDEX) {
                        val clickedCollection =
                            collections[selectedFeaturedCollectionIndex.intValue]
                                ?: return@FeaturedSection
                        val newSearchValue =
                            searchValue.value.text.replace("${clickedCollection.title} ", "")

                        searchValue.value = searchValue.value.copy(
                            text = newSearchValue,
                            selection = TextRange.Zero
                        )
                    }

                    val newSearchValue = "${collections[index]!!.title} " + searchValue.value.text
                    searchValue.value = searchValue.value.copy(
                        text = newSearchValue,
                        selection = TextRange(newSearchValue.length)
                    )
                    selectedFeaturedCollectionIndex.intValue = index
                    onSearch(searchValue.value.text)
                }
            )
        }

        if (shouldShowLoadingProgress.value) {
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

        Spacer(modifier = Modifier.height(Dimens.PADDING_MEDIUM))

        if (!shouldShowNetworkStub.value && !shouldShowNoResultsStub.value) {
            HomePhotoSection(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = Dimens.PADDING_MEDIUM),
                photos = photos,
                onAnyPhotoClicked = onAnyPhotoClicked
            )
        }

        if (shouldShowNetworkStub.value) {
            Spacer(modifier = Modifier.weight(1f))
            NoNetworkStub(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .height(Dimens.NO_NETWORK_STUB_HEIGHT)
                    .width(Dimens.NO_NETWORK_STUB_WIDTH),
                onClick = {
                    onTryAgainRequest()
                    shouldShowNetworkStub.value = false
                }
            )
            Spacer(modifier = Modifier.weight(1f))
        }

        if (shouldShowNoResultsStub.value) {
            Spacer(modifier = Modifier.weight(1f))
            NoResultsStub(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                onClick = {
                    searchValue.value = searchValue.value.copy(text = "")
                    onExplorePhotos()
                }
            )
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

private fun observeCollectionsLoadState(
    collections: LazyPagingItems<FeaturedCollection>,
    isCollectionsLoading: MutableState<Boolean>,
    isCollectionsError: MutableState<Boolean>
) {
    collections.apply {
        when {
            loadState.refresh is LoadState.Loading -> {
                isCollectionsLoading.value = true
                isCollectionsError.value = false
            }

            loadState.refresh is LoadState.NotLoading -> {
                isCollectionsLoading.value = false
                isCollectionsError.value = false
            }

            loadState.refresh is LoadState.Error -> {
                isCollectionsLoading.value = false
                isCollectionsError.value = true
            }

            loadState.append is LoadState.Error -> {
                isCollectionsLoading.value = false
                isCollectionsError.value = true
            }

            loadState.append is LoadState.Loading -> {
                isCollectionsLoading.value = true
                isCollectionsError.value = false
            }

            loadState.append is LoadState.NotLoading -> {
                isCollectionsLoading.value = false
                isCollectionsError.value = false
            }
        }
    }
}

private fun observePhotosLoadState(
    photos: LazyPagingItems<Photo>,
    onCacheRequest: () -> Boolean,
    isPhotosLoading: MutableState<Boolean>,
    isPhotosError: MutableState<Boolean>,
    isHttpError: MutableState<Boolean>,
    shouldShowNetworkStub: MutableState<Boolean>,
    shouldResendPhotosRequest: MutableState<Boolean>
) {
    photos.apply {
        when {
            loadState.refresh is LoadState.Loading -> {
                isPhotosLoading.value = true
            }

            loadState.refresh is LoadState.Error -> {
                isPhotosLoading.value = false
                isPhotosError.value = true
                isHttpError.value = (loadState.refresh as LoadState.Error).error is HttpException
            }

            loadState.refresh is LoadState.NotLoading -> {
                isPhotosLoading.value = false

                val loadStateAppend = loadState.append
                if (loadStateAppend is LoadState.Error) {
                    if (loadStateAppend.error is UnknownHostException) {
                        shouldResendPhotosRequest.value = true

                        if (onCacheRequest()) {
                            shouldShowNetworkStub.value = true
                        }
                    }
                }
            }

            loadState.append is LoadState.Loading -> {
                isPhotosLoading.value = true
            }

            loadState.append is LoadState.Error -> {
                isPhotosLoading.value = false
                isPhotosError.value = true
            }

            loadState.append is LoadState.NotLoading -> {
                isPhotosLoading.value = false
            }
        }
    }
}

@LightAndDarkPreview
@Composable
fun HomeScreenPreview() {
    AppTheme {
        val collections = MutableStateFlow<PagingData<FeaturedCollection>>(
            PagingData.empty()
        ).collectAsLazyPagingItems()

        val photos = MutableStateFlow<PagingData<Photo>>(
            PagingData.empty()
        ).collectAsLazyPagingItems()

        val selectedFeaturedCollectionIndex =
            remember { mutableIntStateOf(Constants.EMPTY_COLLECTION_HEADER_INDEX) }
        val isInternetWasDisconnected = remember { mutableStateOf(true) }
        val networkStatusToastsCount = remember { mutableIntStateOf(0) }

        HomeScreen(
            modifier = Modifier.fillMaxSize(),
            signedInUserDetails = UserDetails(
                userId = "",
                username = null,
                profilePictureUrl = null,
                phoneNumber = null,
                email = ""
            ),
            onCacheRequest = { false },
            isInternetConnected = false,
            isInternetWasDisconnected = isInternetWasDisconnected,
            networkStatusToastsCount = networkStatusToastsCount,
            collections = collections,
            photos = photos,
            searchQuery = "",
            onSearch = { },
            selectedFeaturedCollectionIndex = selectedFeaturedCollectionIndex,
            onHttpError = { },
            onExplorePhotos = { },
            onAnyPhotoClicked = { },
            onCollectionsRequest = { },
            onTryAgainRequest = { },
            onPhotosRequest = { },
            onUserAvatarClicked = { }
        )
    }
}