package com.sdv.kit.pexelsapp.data.remote.interceptor

import com.sdv.kit.pexelsapp.util.Constants
import okhttp3.Interceptor
import okhttp3.Response

class RateLimitInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)

        if (response.code == Constants.TOO_MANY_REQUESTS_CODE) {
            Thread.sleep(Constants.TOO_MANY_REQUESTS_DELAY)
            response.close()
            return chain.proceed(request)
        }

        return response
    }
}
