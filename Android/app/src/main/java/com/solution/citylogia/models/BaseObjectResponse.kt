package com.solution.citylogia.models

data class BaseObjectResponse<T>(
        val statusCode: Long = 200,
        val data: T? = null,
)
