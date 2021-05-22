package com.solution.citylogia.network

import android.content.Context
import com.solution.citylogia.MainActivity
import com.solution.citylogia.StaticContextFactory
import com.solution.citylogia.services.AuthorizationService
import dagger.hilt.android.qualifiers.ApplicationContext
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
        val context = StaticContextFactory.getContext()
        val storage = StorageService(context)
        val authorizationService = AuthorizationService(storage)
        val authInterceptor = AuthInterceptor(authorizationService)

        val client = OkHttpClient.Builder()
                                 .addInterceptor(interceptor)
                                 .addInterceptor(authInterceptor)
                                 .build();

        return Retrofit.Builder()
                       .baseUrl("http://35.209.124.144:8000/api/")
                       .client(client)
                       .addConverterFactory(GsonConverterFactory.create())
                       .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                       .build()
    }
}