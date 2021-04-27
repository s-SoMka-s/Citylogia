package com.solution.citylogia.network.api

import com.solution.citylogia.models.*
import io.reactivex.Single
import retrofit2.http.*

interface IFavoritesApi {
    @POST("Favorites")
    fun makeFavorite(@Body data: Any): Single<BaseObjectResponse<Boolean>>

    @DELETE("Favorites/{id}")
    fun deleteFavorite(@Path("id") id: Long): Single<BaseObjectResponse<Boolean>>
}