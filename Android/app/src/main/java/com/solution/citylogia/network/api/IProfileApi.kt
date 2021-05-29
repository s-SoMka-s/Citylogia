package com.solution.citylogia.network.api

import com.solution.citylogia.models.BaseObjectResponse
import com.solution.citylogia.models.User
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface IProfileApi {
    @GET("Profile")
    fun get(): Single<BaseObjectResponse<User>>

    @POST("Profile/Avatar")
    fun uploadAvatar(@Body data: Any): Single<BaseObjectResponse<Boolean>>
}