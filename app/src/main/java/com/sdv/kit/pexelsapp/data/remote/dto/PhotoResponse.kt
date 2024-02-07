package com.sdv.kit.pexelsapp.data.remote.dto

import com.google.gson.annotations.SerializedName
import com.sdv.kit.pexelsapp.domain.model.Photo

data class PhotoResponse(

    @SerializedName("page")
    val page: Int,

    @SerializedName("per_page")
    val perPage: Int,

    @SerializedName("photos")
    val photos: List<Photo>,

    @SerializedName("next_page")
    val nextPage: String?
)