package com.sdv.kit.pexelsapp.domain.model

import androidx.room.ColumnInfo
import androidx.room.ColumnInfo.Companion.INTEGER
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bookmarked")
data class Bookmarked(

    @PrimaryKey
    @ColumnInfo(name = "photo_id", typeAffinity = INTEGER)
    val photoId: Int,

    @Embedded(prefix = "bookmarked_")
    val photo: Photo,

    @ColumnInfo(name = "value", typeAffinity = INTEGER)
    var value: Boolean,

    @ColumnInfo(name = "page", typeAffinity = INTEGER)
    val page: Int = 1
)