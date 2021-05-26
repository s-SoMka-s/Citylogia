package com.solution.citylogia.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RetrofitSingleton @Inject constructor(private val authInterceptor: AuthInterceptor){
    val retrofit = this.configure();

    private fun configure(): Retrofit {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY

        val client = OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .addInterceptor(authInterceptor)
                .build();

        return Retrofit.Builder()
                //.baseUrl("http://35.209.124.144:8000/api/")
                .baseUrl("http://37.192.40.168:5000/api/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
    }
}