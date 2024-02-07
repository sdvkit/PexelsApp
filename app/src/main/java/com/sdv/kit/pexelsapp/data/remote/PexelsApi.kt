package com.sdv.kit.pexelsapp.data.remote

import com.sdv.kit.pexelsapp.data.remote.dto.FeaturedCollectionsResponse
import com.sdv.kit.pexelsapp.data.remote.dto.PhotoResponse
import com.sdv.kit.pexelsapp.domain.model.Photo
import com.sdv.kit.pexelsapp.util.Constants
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface PexelsApi {

    @GET("collections/featured?per_page=${Constants.FEATURED_PER_PAGE}")
    @Headers("Authorization: ${Constants.PEXELS_API_KEY}")
    suspend fun getFeatured(
        @Query("page") page: Int
    ): FeaturedCollectionsResponse

    @GET("curated?per_page=${Constants.PHOTOS_PER_PAGE}")
    @Headers("Authorization: ${Constants.PEXELS_API_KEY}")
    suspend fun getCurated(
        @Query("page") page: Int
    ): PhotoResponse

    @GET("search?per_page=${Constants.PHOTOS_PER_PAGE}")
    @Headers("Authorization: ${Constants.PEXELS_API_KEY}")
    suspend fun searchPhoto(
        @Query("page") page: Int,
        @Query("query") query: String
    ): PhotoResponse

    @GET("photos/{id}")
    @Headers("Authorization: ${Constants.PEXELS_API_KEY}")
    suspend fun getPhotoById(
        @Path("id") id: Int
    ): Photo?
}