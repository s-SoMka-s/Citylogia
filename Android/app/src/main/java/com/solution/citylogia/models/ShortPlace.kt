package com.solution.citylogia.models

data class ShortPlace(
        val id: Long = 0,
        val type: PlaceType = PlaceType(),
        val latitude: Double = 0.0,
        val longitude: Double = 0.0,
){
    companion object Factory {
        private var lastId: Long = -1;

        fun makeShortPlace(): ShortPlace {
            lastId++;

            return ShortPlace(id = lastId);
        }
    }
}
