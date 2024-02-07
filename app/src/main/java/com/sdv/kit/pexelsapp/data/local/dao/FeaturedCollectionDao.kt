package com.sdv.kit.pexelsapp.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sdv.kit.pexelsapp.domain.model.FeaturedCollection

@Dao
interface FeaturedCollectionDao {

    @Query("SELECT * FROM featured_collections ORDER BY page")
    fun pagingSource(): PagingSource<Int, FeaturedCollection>

    @Query("SELECT COUNT(id) FROM featured_collections")
    fun getCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg featuredCollections: FeaturedCollection)

    @Query("DELETE FROM featured_collections")
    suspend fun clearAll()

    @Query("SELECT MAX(last_modified) FROM featured_collections")
    suspend fun lastUpdated(): Long
}