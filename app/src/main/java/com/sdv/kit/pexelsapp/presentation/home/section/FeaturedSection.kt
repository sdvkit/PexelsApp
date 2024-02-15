package com.sdv.kit.pexelsapp.presentation.home.section

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.sdv.kit.pexelsapp.domain.model.FeaturedCollection
import com.sdv.kit.pexelsapp.presentation.Dimens
import com.sdv.kit.pexelsapp.presentation.anim.bounceClickEffect
import com.sdv.kit.pexelsapp.presentation.annotation.LightAndDarkPreview
import com.sdv.kit.pexelsapp.presentation.common.FeaturedCollectionHeader
import com.sdv.kit.pexelsapp.presentation.ui.theme.AppTheme
import com.sdv.kit.pexelsapp.util.Constants
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

@Composable
fun FeaturedSection(
    modifier: Modifier = Modifier,
    collections: LazyPagingItems<FeaturedCollection>,
    selectedItemIndex: Int,
    onActiveItemClicked: (FeaturedCollection) -> Unit,
    onAnyItemClicked: (Int) -> Unit
) {
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    LazyRow(
        modifier = modifier.scrollable(
            orientation = Orientation.Horizontal,
            state = rememberScrollableState { delta ->
                coroutineScope.launch {
                    listState.scrollBy(-delta / 5)
                }
                delta
            }
        ),
        state = listState,
        userScrollEnabled = false
    ) {
        if (collections.itemCount > 0) {
            item {
                Spacer(modifier = Modifier.width(Dimens.PADDING_SMALL))

                if (selectedItemIndex != Constants.EMPTY_COLLECTION_HEADER_INDEX && collections[selectedItemIndex] != null) {
                    FeaturedCollectionHeader(
                        modifier = Modifier
                            .padding(start = Dimens.PADDING_SMALL)
                            .bounceClickEffect(),
                        text = collections[selectedItemIndex]!!.title,
                        isActive = true,
                        onClick = {
                            onActiveItemClicked(collections[selectedItemIndex]!!)
                        }
                    )
                }
            }
            items(collections.itemCount) { index ->
                if (collections[index] != null) {
                    AnimatedVisibility(
                        visible = index != selectedItemIndex,
                        exit = scaleOut(
                            animationSpec = tween(
                                durationMillis = 100
                            )
                        )
                    ) {
                        FeaturedCollectionHeader(
                            modifier = Modifier
                                .padding(start = Dimens.PADDING_SMALL)
                                .bounceClickEffect(),
                            text = collections[index]!!.title,
                            isActive = false,
                            onClick = {
                                coroutineScope.launch {
                                    onAnyItemClicked(index)
                                    listState.animateScrollToItem(index = 0)
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

@LightAndDarkPreview
@Composable
fun FeaturedSectionPreview() {
    AppTheme {
        Surface(
            modifier = Modifier.background(
                color = AppTheme.colors.surface
            )
        ) {
            val items = MutableStateFlow<PagingData<FeaturedCollection>>(
                PagingData.empty()
            ).collectAsLazyPagingItems()

            FeaturedSection(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(all = Dimens.PADDING_MEDIUM),
                collections = items,
                selectedItemIndex = 0,
                onActiveItemClicked = { },
                onAnyItemClicked = { }
            )
        }
    }
}