package com.sdv.kit.pexelsapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sdv.kit.pexelsapp.domain.model.Bookmarked

@Dao
interface BookmarkedDao {

    @Query("SELECT * FROM bookmarked WHERE page=:page AND value=1 ORDER BY page")
    suspend fun getAllByPage(page: Int): List<Bookmarked>

    @Query("SELECT MAX(page) FROM bookmarked")
    fun getLastPage(): Int

    @Query("SELECT COUNT(photo_id) FROM bookmarked")
    suspend fun getCount(): Int

    @Query("SELECT * FROM bookmarked WHERE photo_id=:photoId")
    suspend fun getById(photoId: Int): Bookmarked?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg bookmarked: Bookmarked)
}