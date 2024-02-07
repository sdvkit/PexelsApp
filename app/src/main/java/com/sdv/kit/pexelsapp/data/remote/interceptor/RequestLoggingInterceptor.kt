package com.sdv.kit.pexelsapp.data.remote.interceptor

import android.util.Log
import com.sdv.kit.pexelsapp.util.Constants
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class RequestLoggingInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)

        val message = buildMessage(request = request, response = response)
        Log.i(LOG_TAG, message)

        return response
    }

    private fun buildMessage(request: Request, response: Response): String {
        val method = request.method
        val endpoint = request.url.toString().replace(Constants.PEXELS_API_BASE_URL, "")
        val responseStatusCode = response.code
        return "[${method}] ($responseStatusCode) - $endpoint"
    }

    companion object {
        private const val LOG_TAG = "REQUEST_LOGGER"
    }
}
