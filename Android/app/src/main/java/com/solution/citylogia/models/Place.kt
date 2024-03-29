package com.solution.citylogia.models

import kotlinx.serialization.Serializable

@Serializable
data class Place(
        val id: Long = 0,
        val mark: Long = 0,
        val name: String? = null,
        var is_favorite: Boolean = false,
        val description: String? = null,
        val short_description: String? = null,
        val type: PlaceType = PlaceType(),
        var city: String = "",
        var street: String = "",
        var house: Long = 0,
        val latitude: Double = 0.0,
        val longitude: Double = 0.0,
        val photos: BaseCollectionClass<Photo> = BaseCollectionClass(),
        val reviews: BaseCollectionClass<Review> = BaseCollectionClass(),
) {
    companion object Factory {
        private var lastId: Long = -1;

        fun makePlace(): Place {
            lastId++;

            return Place(id = lastId);
        }
    }
}