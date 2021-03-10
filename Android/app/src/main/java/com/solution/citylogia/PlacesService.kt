package com.solution.citylogia

import com.google.gson.Gson
import com.google.gson.stream.JsonToken
import kotlinx.serialization.*;
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray

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
        val gs = Gson();
        val data = gs.fromJson(content, Places::class.java);

    }
}