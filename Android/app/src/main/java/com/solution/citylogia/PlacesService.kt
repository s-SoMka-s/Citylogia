package com.solution.citylogia

import com.google.gson.Gson
import com.google.gson.stream.JsonToken
import com.solution.citylogia.data.remote.place.PlaceApi
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.serialization.*;
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray

import okhttp3.*
import java.io.IOException

class PlacesService {
    fun getPlaces(placeApi: PlaceApi) {
        placeApi.getPlaces().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({
            println(it);
        },{})
    }
}