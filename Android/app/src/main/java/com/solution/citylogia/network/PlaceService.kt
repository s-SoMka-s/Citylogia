package com.solution.citylogia.network

import com.solution.citylogia.network.api.IPlaceApi
import io.reactivex.schedulers.Schedulers

class PlaceService() {
    private var placeApi: IPlaceApi

    init {
        val retrofit = RetrofitSingleton.retrofit
        this.placeApi = retrofit.create(IPlaceApi::class.java)
    }

    fun drawAllPlaces() {
        this.placeApi.getAllPlaces()
                .subscribeOn(Schedulers.newThread())
                .subscribe({ places ->
                    println(places.data.elements)
                }, {
                    // OnError
                })
    }

    fun getPLacesNear(latitude: Double, longitude: Double, radius: Long) {
        this.placeApi.getPlacesNear(latitude, longitude, radius)
                .subscribeOn(Schedulers.newThread())
                .subscribe({ places ->
                    println(places.data.elements)
                }, {
                    // OnError
                })
    }

    fun getPlaceInfo(placeId: Long) {
        this.placeApi.getPlaceInfo(1)
                .subscribeOn(Schedulers.newThread())
                .subscribe({ value -> println(value) }, {})
    }
}