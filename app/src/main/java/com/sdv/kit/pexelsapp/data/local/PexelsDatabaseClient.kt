package com.sdv.kit.pexelsapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.sdv.kit.pexelsapp.data.local.dao.BookmarkedDao
import com.sdv.kit.pexelsapp.data.local.dao.FeaturedCollectionDao
import com.sdv.kit.pexelsapp.data.local.dao.PhotoDao
import com.sdv.kit.pexelsapp.domain.model.Bookmarked
import com.sdv.kit.pexelsapp.domain.model.FeaturedCollection
import com.sdv.kit.pexelsapp.domain.model.Photo

@Database(
    entities = [
        FeaturedCollection::class,
        Photo::class,
        Bookmarked::class
    ],
    version = 1
)
abstract class PexelsDatabaseClient : RoomDatabase() {
    abstract fun featuredCollectionDao(): FeaturedCollectionDao
    abstract fun photoDao(): PhotoDao
    abstract fun bookmarkedDao(): BookmarkedDao
}