package com.solution.citylogia.models

import java.time.OffsetDateTime

data class Review(
        val id: Long = 0,
        val body: String = "",
        val mark: Long = 0,
        val published_at: OffsetDateTime = OffsetDateTime.now(),
        val author: User = User(),
)
