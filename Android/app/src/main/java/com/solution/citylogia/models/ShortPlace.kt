package com.solution.citylogia.models


data class ShortPlace(
        var id: Long = 0,
        var type: PlaceType = PlaceType(),
        var latitude: Double = 0.0,
        var longitude: Double = 0.0,
        var distanceTo: Double? = null,
        var address: String = "",
        var name: String? = null,
){

    companion object Factory {
        private var lastId: Long = -1;

        fun makeShortPlace(): ShortPlace {
            lastId++;

            return ShortPlace(id = lastId);
        }
    }
}
