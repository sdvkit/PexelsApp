package com.sdv.kit.pexelsapp.domain.model

import androidx.room.ColumnInfo
import androidx.room.ColumnInfo.Companion.INTEGER
import androidx.room.ColumnInfo.Companion.TEXT
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.sdv.kit.pexelsapp.data.remote.Exclude

@Entity(tableName = "photos")
data class Photo(

    @PrimaryKey
    @ColumnInfo(name = "id", typeAffinity = INTEGER)
    @SerializedName("id")
    val photoId: Int,

    @ColumnInfo(name = "width", typeAffinity = INTEGER)
    @SerializedName("width")
    val width: Int,

    @ColumnInfo(name = "height", typeAffinity = INTEGER)
    @SerializedName("height")
    val height: Int,

    @ColumnInfo(name = "photographer", typeAffinity = TEXT)
    @SerializedName("photographer")
    val photographer: String,

    @Embedded
    @SerializedName("src")
    val src: PhotoSrc,

    @ColumnInfo(name = "alt", typeAffinity = TEXT)
    @SerializedName("alt")
    val alt: String,

    @Exclude
    @ColumnInfo(name = "page", typeAffinity = INTEGER)
    var page: Int = 1,

    @Exclude
    @ColumnInfo(name = "last_modified")
    var lastModified: Long = System.currentTimeMillis()
) {
    constructor() : this(
        photoId = 0,
        width = 0,
        height = 0,
        photographer = "",
        src = PhotoSrc(original = ""),
        alt = ""
    )
}

data class PhotoSrc(

    @ColumnInfo(name = "original", typeAffinity = TEXT)
    @SerializedName("original")
    val original: String
)