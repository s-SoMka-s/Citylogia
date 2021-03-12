package com.solution.citylogia
import com.solution.citylogia.data.remote.place.PlaceApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class Configurator {
    lateinit var placeApi: PlaceApi

    fun configureRetrofit() {
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

        val client = OkHttpClient.Builder()
                                 .addInterceptor(interceptor)
                                 .build();

        val retrofit = Retrofit.Builder()
                               .baseUrl("http://84.201.147.252:8080/api")
                               .client(client)
                               .addConverterFactory(GsonConverterFactory.create())
                               .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                               .build();

        this.placeApi = retrofit.create(PlaceApi::class.java);
    }
}