package com.solution.citylogia.models

import kotlinx.serialization.Serializable
import java.time.OffsetDateTime

@Serializable
data class Review(
        val id: Long = 0,
        val body: String = "Здесь должно быть содержание отзыва",
        val mark: Double = 0.0,
        val published_at: String? = null,
        val author: User = User()
) {
    companion object Factory {
        var lastId: Long = -1;

        fun makeReview(): Review {
            lastId++;

            return Review(id = lastId);
        }
    }
}
