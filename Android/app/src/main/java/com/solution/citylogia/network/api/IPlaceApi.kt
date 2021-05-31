package com.solution.citylogia.network.api

import com.solution.citylogia.models.*
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface IPlaceApi {
    @GET("Places")
    fun getAllPlaces(@Query("longitude") longitude: Double? = null, @Query("latitude") latitude: Double? = null, @Query("radius_in_km") radius: Double? = null, @Query("type_ids") typeIds: Iterable<Long>? = null): Single<BaseCollectionResponse<Place>>

    @GET("Places/{id}")
    fun getPlaceInfo(@Path("id") placeId: Long): Single<BaseObjectResponse<Place>>

    @GET("Places/Types")
    fun getPlaceTypes(): Single<BaseCollectionResponse<PlaceType>>
}