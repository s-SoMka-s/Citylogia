package com.solution.citylogia.network.api

import com.solution.citylogia.models.BaseObjectResponse
import com.solution.citylogia.models.User
import io.reactivex.Single
import retrofit2.http.GET

interface IProfileApi {
    @GET("Profile")
    fun get(): Single<BaseObjectResponse<User>>
}