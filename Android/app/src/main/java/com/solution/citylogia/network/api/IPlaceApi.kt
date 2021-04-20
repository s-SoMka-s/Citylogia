package com.solution.citylogia.network.api

import com.solution.citylogia.models.BaseCollectionResponse
import com.solution.citylogia.models.BaseObjectResponse
import com.solution.citylogia.models.Place
import com.solution.citylogia.models.ShortPlace
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface IPlaceApi {
    @GET("Map/Places")
    fun getAllPlaces(): Single<BaseCollectionResponse<ShortPlace>>

    @GET("Map/Places/{id}")
    fun getPlaceInfo(@Path("id") placeId: Long): Single<BaseObjectResponse<Place>>

    @GET("Map/Places/Near")
    fun getPlacesNear(@Query("latitude") latitude: Double, @Query("longitude") longitude: Double, @Query("radius") radius: Long): Single<BaseCollectionResponse<Place>>
}