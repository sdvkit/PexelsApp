package com.sdv.kit.pexelsapp.data.remote

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.sdv.kit.pexelsapp.data.remote.interceptor.RateLimitInterceptor
import com.sdv.kit.pexelsapp.data.remote.interceptor.RequestLoggingInterceptor
import com.sdv.kit.pexelsapp.util.Constants
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object PexelsApiClient {

    private val gson: Gson = GsonBuilder()
        .setExclusionStrategies(AnnotationExclusionStrategy())
        .create()

    private val httpClient = OkHttpClient.Builder()
        .addInterceptor(RateLimitInterceptor())
        .addInterceptor(RequestLoggingInterceptor())
        .build()

    val client: Retrofit = Retrofit.Builder()
        .baseUrl(Constants.PEXELS_API_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .client(httpClient)
        .build()
}