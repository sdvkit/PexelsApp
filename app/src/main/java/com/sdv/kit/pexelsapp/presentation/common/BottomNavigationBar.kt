package com.sdv.kit.pexelsapp.presentation.common

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import com.sdv.kit.pexelsapp.R
import com.sdv.kit.pexelsapp.presentation.Dimens
import com.sdv.kit.pexelsapp.presentation.anim.bounceClickEffect
import com.sdv.kit.pexelsapp.presentation.annotation.LightAndDarkPreview
import com.sdv.kit.pexelsapp.presentation.navigation.NavigationItem
import com.sdv.kit.pexelsapp.presentation.ui.theme.AppTheme

@Composable
fun BottomNavigationBar(
    modifier: Modifier = Modifier,
    items: List<NavigationItem>,
    activeItemName: MutableState<String>
) {
    Row(modifier = modifier) {
        for (item in items) {
            BottomNavigationBarItem(
                modifier = Modifier
                    .weight(1f)
                    .height(Dimens.BOTTOM_NAVIGATION_HEIGHT),
                icon = item.icon,
                activeIcon = item.activeIcon,
                isActive = activeItemName.value == item.name,
                onClick = {
                    activeItemName.value = item.name
                    item.onClick()
                }
            )
        }
    }
}

@Composable
fun BottomNavigationBarItem(
    modifier: Modifier = Modifier,
    @DrawableRes icon: Int,
    @DrawableRes activeIcon: Int = icon,
    isActive: Boolean,
    onClick: () -> Unit
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when (isActive) {
            true -> {
                BottomNavigationBarItemIndicator()
            }
            else -> {
                Spacer(modifier = Modifier.height(Dimens.BOTTOM_NAVIGATION_ITEM_INDICATOR_HEIGHT))
            }
        }
        Spacer(modifier = Modifier.weight(1f))
        Button(
            modifier = Modifier.bounceClickEffect(),
            onClick = onClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent
            )
        ) {
            Image(
                modifier = Modifier.size(Dimens.BOTTOM_NAVIGATION_ITEM_ICON_SIZE),
                painter = painterResource(if (isActive) activeIcon else icon),
                contentDescription = null
            )
        }
        Spacer(modifier = Modifier.height(Dimens.BOTTOM_NAVIGATION_ITEM_INDICATOR_HEIGHT))
    }
}

@Composable
fun BottomNavigationBarItemIndicator(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .width(Dimens.BOTTOM_NAVIGATION_ITEM_INDICATOR_WIDTH)
            .height(Dimens.BOTTOM_NAVIGATION_ITEM_INDICATOR_HEIGHT)
            .background(
                color = AppTheme.colors.primary
            )
    )
}

@LightAndDarkPreview
@Composable
fun BottomNavigationBarItemPreview() {
    AppTheme {
        Surface(
            modifier = Modifier
                .padding(all = Dimens.PADDING_MEDIUM)
                .background(
                    color = AppTheme.colors.surface
                )
        ) {
            BottomNavigationBarItem(
                modifier = Modifier.height(Dimens.BOTTOM_NAVIGATION_HEIGHT),
                icon = R.drawable.ic_home_inactive,
                isActive = true,
                onClick = { }
            )
        }
    }
}

@LightAndDarkPreview
@Composable
fun BottomNavigationBarPreview() {
    AppTheme {
        val activeItemName = remember {
            mutableStateOf("Home")
        }

        Surface(
            modifier = Modifier.background(
                color = AppTheme.colors.surface
            )
        ) {
            BottomNavigationBar(
                modifier = Modifier.fillMaxWidth(),
                items = listOf(
                    NavigationItem(
                        name = "Home",
                        icon = R.drawable.ic_home_inactive,
                        activeIcon = R.drawable.ic_home_active,
                        onClick = { }
                    ),
                    NavigationItem(
                        name = "Bookmark",
                        icon = R.drawable.ic_bookmark_inactive,
                        activeIcon = R.drawable.ic_bookmark_active,
                        onClick = { }
                    )
                ),
                activeItemName = activeItemName
            )
        }
    }
}