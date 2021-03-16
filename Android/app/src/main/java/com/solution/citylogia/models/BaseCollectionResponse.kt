package com.solution.citylogia.models

import com.google.gson.annotations.SerializedName

data class BaseCollectionResponse<T>(
        val status_code: Long = 200,
        val data: BaseCollectionClass<T> = BaseCollectionClass(),
)