package com.solution.citylogia.network.api

import com.solution.citylogia.models.BaseCollectionResponse
import com.solution.citylogia.models.BaseObjectResponse
import com.solution.citylogia.models.Review
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface IReviewsApi {
    @GET("Reviews/{placeId}")
    fun get(@Path("placeId") placeId: Long) : Single<BaseCollectionResponse<Review>>

    @POST("Reviews/{placeId}")
    fun addReview(@Path("placeId") placeId: Long, @Body data: Any): Single<BaseObjectResponse<Boolean>>
}