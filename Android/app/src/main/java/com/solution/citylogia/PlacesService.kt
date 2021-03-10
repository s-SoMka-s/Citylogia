package com.solution.citylogia

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import okhttp3.*
import java.io.IOException

class PlacesService {
    fun getPlaces() {
        Thread(){getPlacess()}.start()
    }

    fun getPlacess() {
        val client = OkHttpClient()
        val request: Request = Request.Builder().url("http://84.201.147.252:8080/api/Map/Places").build()
        val call = client.newCall(request)
        val response = call.execute();
        val content = response.body!!.string()
        val data = Json.decodeFromString<Place>(content);
        println(data);
    }
}