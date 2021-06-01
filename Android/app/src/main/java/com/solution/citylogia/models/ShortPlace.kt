package com.solution.citylogia.models

data class ShortPlace(
        var id: Long = 0,
        var name: String? = null,
        var is_favorite: Boolean = false,
        var main_photo: Photo? = null,
        var type: PlaceType = PlaceType(),
        var latitude: Double = 0.0,
        var longitude: Double = 0.0,
        var distanceTo: Double? = null,
        var city: String = "",
        var street: String = "",
        var house: Long = 0,
){

    companion object Factory {
        private var lastId: Long = -1;

        fun makeShortPlace(): ShortPlace {
            lastId++;

            return ShortPlace(id = lastId);
        }
    }
}
