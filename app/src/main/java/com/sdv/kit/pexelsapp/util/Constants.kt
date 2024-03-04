package com.sdv.kit.pexelsapp.util

import com.sdv.kit.pexelsapp.BuildConfig

object Constants {

    const val DB_NAME = "pexels_db"

    const val PEXELS_API_KEY = BuildConfig.PEXELS_API_KEY
    const val PEXELS_API_BASE_URL = "https://api.pexels.com/v1/"

    const val EMPTY_COLLECTION_HEADER_INDEX = -1

    const val USER_FRIENDLY_SEARCH_INTERVAL = 1200L

    const val PAGE_CONFIG_PHOTO_SIZE = 5
    const val PAGE_CONFIG_FEATURED_SIZE = 2
    const val PHOTOS_PER_PAGE = 30
    const val FEATURED_PER_PAGE = 7

    const val CHECK_INTERNET_HOST = "8.8.8.8"
    const val CHECK_INTERNET_PORT = 53
    const val CHECK_INTERNET_TIMEOUT = 1500

    const val TOO_MANY_REQUESTS_CODE = 429
    const val TOO_MANY_REQUESTS_DELAY = 5000L

    const val GOOGLE_WEB_CLIENT_ID = BuildConfig.GOOGLE_WEB_CLIENT_ID

    const val GOOGLE_FCM_API_BASE_URL = "https://fcm.googleapis.com/fcm/"
    const val GOOGLE_FCM_KEY = BuildConfig.GOOGLE_FCM_KEY

    const val IMAGE_NAME_PREFIX = "pexels_photo_"

    const val SHARED_PREF_NAME = "pexels_shared_pref"
}