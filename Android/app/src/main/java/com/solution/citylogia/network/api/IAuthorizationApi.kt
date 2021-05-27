package com.solution.citylogia.network.api

import com.solution.citylogia.models.BaseObjectResponse
import com.solution.citylogia.network.models.input.AuthResponse
import com.solution.citylogia.network.models.output.LoginParameters
import com.solution.citylogia.network.models.output.RegistrationParameters
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.POST

interface IAuthorizationApi {
    @POST("Auth/Email")
    fun login(@Body data: LoginParameters): Single<BaseObjectResponse<AuthResponse>>

    @POST("Auth/Register")
    fun register(@Body data: RegistrationParameters): Single<BaseObjectResponse<AuthResponse>>
}