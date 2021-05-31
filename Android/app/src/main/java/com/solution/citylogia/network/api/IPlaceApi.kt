package com.solution.citylogia.network.api

import com.solution.citylogia.models.*
import io.reactivex.Single
import retrofit2.http.*

interface IPlaceApi {
    @GET("Places")
    fun getAllPlaces(@Query("longitude") longitude: Double? = null, @Query("latitude") latitude: Double? = null, @Query("radius_in_km") radius: Double? = null, @Query("type_ids") typeIds: Iterable<Long>? = null, @Query("only_approved") onlyNotReviewed: Boolean = true): Single<BaseCollectionResponse<Place>>

    @POST("Places")
    fun addPlace(@Body data:Any): Single<BaseObjectResponse<Boolean>>

    @GET("Places/{id}")
    fun getPlaceInfo(@Path("id") placeId: Long): Single<BaseObjectResponse<Place>>

    @GET("Places/Types")
    fun getPlaceTypes(): Single<BaseCollectionResponse<PlaceType>>
}