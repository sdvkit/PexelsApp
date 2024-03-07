package com.sdv.kit.pexelsapp.presentation.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.sdv.kit.pexelsapp.R
import com.sdv.kit.pexelsapp.presentation.Dimens
import com.sdv.kit.pexelsapp.presentation.common.BottomNavigationBar
import com.sdv.kit.pexelsapp.presentation.navigation.graph.bookmarksNavGraph
import com.sdv.kit.pexelsapp.presentation.navigation.graph.cameraGraph
import com.sdv.kit.pexelsapp.presentation.navigation.graph.chatNavGraph
import com.sdv.kit.pexelsapp.presentation.navigation.graph.conversationNavGraph
import com.sdv.kit.pexelsapp.presentation.navigation.graph.detailsGraph
import com.sdv.kit.pexelsapp.presentation.navigation.graph.homeNavGraph
import com.sdv.kit.pexelsapp.presentation.navigation.graph.loginNavGraph
import com.sdv.kit.pexelsapp.presentation.navigation.graph.profileNavGraph
import com.sdv.kit.pexelsapp.presentation.ui.theme.AppTheme
import com.sdv.kit.pexelsapp.presentation.ui.theme.Black

@Composable
fun NavGraph(startDestination: NavRoute) {
    val navController = rememberNavController()
    val isBottomNavigationBarVisible = remember { mutableStateOf(false) }
    val activeItemName = remember { mutableStateOf(startDestination.route) }

    val navItems = listOf(
        NavigationItem(
            name = NavRoute.HomeScreen.route,
            icon = R.drawable.ic_home_inactive,
            activeIcon = R.drawable.ic_home_active,
            onClick = {
                navController.navigate(route = NavRoute.HomeScreen.route) {
                    popUpTo(navController.graph.id) {
                        inclusive = true
                    }
                }
            }
        ),
        NavigationItem(
            name = NavRoute.ChatScreen.route,
            icon = R.drawable.ic_chat_inactive,
            activeIcon = R.drawable.ic_chat_active,
            onClick = {
                navController.navigate(route = NavRoute.ChatScreen.route) {
                    popUpTo(navController.graph.id) {
                        inclusive = true
                    }
                }
            }
        ),
        NavigationItem(
            name = NavRoute.BookmarksScreen.route,
            icon = R.drawable.ic_bookmark_inactive,
            activeIcon = R.drawable.ic_bookmark_active,
            onClick = {
                navController.navigate(route = NavRoute.BookmarksScreen.route) {
                    popUpTo(navController.graph.id) {
                        inclusive = true
                    }
                }
            }
        )
    )

    Scaffold(
        bottomBar = {
            if (isBottomNavigationBarVisible.value) {
                BottomNavigationBar(
                    modifier = Modifier
                        .shadow(
                            elevation = Dimens.BOTTOM_NAVIGATION_ELEVATION,
                            ambientColor = Black
                        )
                        .background(color = AppTheme.colors.surface)
                        .fillMaxWidth()
                        .height(Dimens.BOTTOM_NAVIGATION_HEIGHT),
                    items = navItems,
                    activeItemName = activeItemName
                )
            }
        }
    ) { paddings ->
        NavHost(
            modifier = Modifier.padding(paddings),
            navController = navController,
            startDestination = startDestination.route
        ) {
            loginNavGraph(
                navController = navController,
                isBottomNavigationBarVisible = isBottomNavigationBarVisible
            )
            homeNavGraph(
                navController = navController,
                isBottomNavigationBarVisible = isBottomNavigationBarVisible,
                activeItemName = activeItemName
            )
            profileNavGraph(
                navController = navController,
                isBottomNavigationBarVisible = isBottomNavigationBarVisible
            )
            chatNavGraph(
                navController = navController,
                isBottomNavigationBarVisible = isBottomNavigationBarVisible,
                activeItemName = activeItemName
            )
            conversationNavGraph(
                navController = navController,
                isBottomNavigationBarVisible = isBottomNavigationBarVisible
            )
            cameraGraph(
                navController = navController,
                isBottomNavigationBarVisible = isBottomNavigationBarVisible
            )
            bookmarksNavGraph(
                navController = navController,
                isBottomNavigationBarVisible = isBottomNavigationBarVisible,
                activeItemName = activeItemName
            )
            detailsGraph(
                navController = navController,
                isBottomNavigationBarVisible = isBottomNavigationBarVisible
            )
        }
    }
}