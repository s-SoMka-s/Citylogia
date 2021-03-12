package com.solution.citylogia.data.remote.place

import com.google.android.libraries.places.api.Places
import io.reactivex.Single
import retrofit2.http.GET

interface PlaceApi {
    @GET("/Map/Places")
    fun getPlaces(): Single<Places>
}