package com.solution.citylogia.models

data class BaseCollectionResponse<T> (
    val statusCode: Long = 200,
    val data: BaseCollectionClass<T> = BaseCollectionClass(),
)