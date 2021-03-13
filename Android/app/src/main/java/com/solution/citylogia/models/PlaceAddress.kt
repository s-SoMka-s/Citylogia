package com.solution.citylogia.models

data class PlaceAddress(
        val latitude: Double = 0.0,
        val longitude: Double = 0.0,
        val country: String = "",
        val province: String = "",
        val city: String = "",
        val district: String = "",
        val street: String = "",
        val house: Long = 0,
        val flat: Long? = null,
        val postcode: Long = 0,
)
