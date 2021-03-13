package com.solution.citylogia.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitSingleton {
    val retrofit = this.configureRetrofit()

    private fun configureRetrofit(): Retrofit {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY

        val client = OkHttpClient.Builder()
                                 .addInterceptor(interceptor)
                                 .build();

        return Retrofit.Builder()
                       .baseUrl("http://84.201.147.252:8080/api")
                       .client(client)
                       .addConverterFactory(GsonConverterFactory.create())
                       .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                       .build()
    }
}