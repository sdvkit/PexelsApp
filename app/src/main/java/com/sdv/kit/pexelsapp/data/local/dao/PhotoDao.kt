package com.sdv.kit.pexelsapp.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sdv.kit.pexelsapp.domain.model.Photo

@Dao
interface PhotoDao {

    @Query("SELECT * FROM photos ORDER BY page")
    fun pagingSource(): PagingSource<Int, Photo>

    @Query("SELECT COUNT(id) FROM photos")
    fun getCount(): Int

    @Query("SELECT * FROM photos WHERE id=:photoId")
    suspend fun getById(photoId: Int): Photo

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg photo: Photo)

    @Query("DELETE FROM photos")
    suspend fun clearAll()

    @Query("SELECT MAX(last_modified) FROM photos")
    suspend fun lastUpdated(): Long
}