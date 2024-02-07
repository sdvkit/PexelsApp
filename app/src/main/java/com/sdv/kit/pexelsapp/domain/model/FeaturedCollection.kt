package com.sdv.kit.pexelsapp.domain.model

import androidx.room.ColumnInfo
import androidx.room.ColumnInfo.Companion.INTEGER
import androidx.room.ColumnInfo.Companion.TEXT
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.sdv.kit.pexelsapp.data.remote.Exclude

@Entity(tableName = "featured_collections")
data class FeaturedCollection(

    @PrimaryKey
    @ColumnInfo(name = "id", typeAffinity = TEXT)
    @SerializedName("id")
    val id: String,

    @ColumnInfo(name = "title", typeAffinity = TEXT)
    @SerializedName("title")
    val title: String,

    @ColumnInfo(name = "description", typeAffinity = TEXT)
    @SerializedName("description")
    val description: String?,

    @ColumnInfo(name = "private", typeAffinity = INTEGER)
    @SerializedName("private")
    val private: Boolean,

    @ColumnInfo(name = "media_count", typeAffinity = INTEGER)
    @SerializedName("media_count")
    val mediaCount: Int,

    @ColumnInfo(name = "photos_count", typeAffinity = INTEGER)
    @SerializedName("photos_count")
    val photosCount: Int,

    @ColumnInfo(name = "videos_count", typeAffinity = INTEGER)
    @SerializedName("videos_count")
    val videosCount: Int,

    @Exclude
    @ColumnInfo(name = "page", typeAffinity = INTEGER)
    var page: Int = 1,

    @Exclude
    @ColumnInfo(name = "last_modified")
    var lastModified: Long = System.currentTimeMillis()
)