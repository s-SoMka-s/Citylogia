package com.solution.citylogia.network

import com.google.gson.GsonBuilder
import com.solution.citylogia.models.BaseObjectResponse
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ResponseInterceptor @Inject constructor() : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalResponse = chain.proceed(chain.request())
        val gson = GsonBuilder().setPrettyPrinting().create()
        var body = originalResponse.body!!
        var r = gson.fromJson(body.string(), BaseObjectResponse::class.java);
        println(originalResponse)

        return originalResponse
    }
}