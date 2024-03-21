package com.sdv.kit.pexelsapp.presentation.navigation

sealed class NavRoute(val route: String) {

    data object LoginScreen : NavRoute(route = "login_screen")

    data object HomeScreen : NavRoute(route = "home_screen")

    data object BookmarksScreen : NavRoute(route = "bookmarks_screen")

    data object ChatScreen : NavRoute(route = "chat_screen")

    data object ConversationScreen : NavRoute(route = "chat_screen/conversation")

    data object ProfileScreen : NavRoute(route = "profile_screen")

    data object CameraScreen : NavRoute(route = "profile_screen/camera")

    data class DetailsScreen(
        private val screenFrom: String,
        private val photoId: Int
    ) : NavRoute(route = "$screenFrom/details_screen/$photoId") {

        companion object {
            const val baseRoute: String = "{$SCREEN_FROM_ARG}/details_screen/{$PHOTO_ID_ARG}"
        }
    }

    companion object {
        const val SEARCH_QUERY_ARG = "query"
        const val SELECTED_COLLECTION_ARG = "selectedCollection"
        const val PHOTO_ID_ARG = "photoId"
        const val SCREEN_FROM_ARG = "screenFrom"
        const val CLICKED_USER_DETAILS = "clickedUserDetails"
        const val CLICKED_CHAT_DETAILS = "clickedChatDetails"
    }
}