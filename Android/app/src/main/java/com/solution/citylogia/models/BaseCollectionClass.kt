package com.solution.citylogia.models

data class BaseCollectionClass<T>(
        val count: Long = 0,
        val elements: List<T>? = null,
)
