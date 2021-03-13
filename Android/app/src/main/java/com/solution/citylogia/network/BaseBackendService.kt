package com.solution.citylogia.network

import com.solution.citylogia.data.remote.place.PlaceApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class BaseBackendService {
    val retrofit = RetrofitSingleton.retrofit
}