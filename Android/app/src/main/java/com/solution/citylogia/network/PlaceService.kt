package com.solution.citylogia.network

import com.solution.citylogia.models.Place
import com.solution.citylogia.network.api.IPlaceApi

class PlaceService() {
    private var placeApi: IPlaceApi

    init {
        val retrofit = RetrofitSingleton.retrofit
        this.placeApi = retrofit.create(IPlaceApi::class.java)
    }

    fun getAllPlaces(): List<Place> {
        TODO("Not yet implemented")
    }

    fun getPLacesNear(latitude: Double, longitude: Double,radius: Long): List<Place> {
        TODO("Not yet implemented")
    }

    fun getPlaceInfo(placeId: Long): Place {
        TODO("Not yet implemented")
    }
}