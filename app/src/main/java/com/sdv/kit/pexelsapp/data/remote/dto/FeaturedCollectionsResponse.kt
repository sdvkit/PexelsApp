package com.sdv.kit.pexelsapp.data.remote.dto

import com.google.gson.annotations.SerializedName
import com.sdv.kit.pexelsapp.domain.model.FeaturedCollection

data class FeaturedCollectionsResponse(

    @SerializedName("page")
    val page: Int,

    @SerializedName("per_page")
    val perPage: Int,

    @SerializedName("collections")
    val featuredCollections: List<FeaturedCollection>,

    @SerializedName("total_results")
    val totalResults: Int,

    @SerializedName("next_page")
    val nextPage: String?
)