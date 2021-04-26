package com.solution.citylogia.network.api

import com.solution.citylogia.models.BaseObjectResponse
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

interface IReviewsApi {
    @POST("Reviews/{placeId}")
    fun addReview(@Path("placeId") placeId: Long, @Body data: Any): Single<BaseObjectResponse<Boolean>>
}