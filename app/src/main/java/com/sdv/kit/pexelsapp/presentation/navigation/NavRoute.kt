package com.sdv.kit.pexelsapp.presentation.navigation

sealed class NavRoute(val route: String) {

    data object HomeScreen : NavRoute(route = "home_screen")

    data object BookmarksScreen : NavRoute(route = "bookmarks_screen")

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
    }
}