package com.example.relay

import com.example.relay.ApplicationClass.Companion.prefs
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

class XAccessTokenInterceptor : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val builder: Request.Builder = chain.request().newBuilder()
        val accessToken: String? = prefs.getString("accessToken", "")
        if (accessToken != null) {
            builder.addHeader("Authorization", accessToken)
        }
        return chain.proceed(builder.build())
    }
}