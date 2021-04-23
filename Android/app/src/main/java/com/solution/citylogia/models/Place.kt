package com.solution.citylogia.models

data class Place(
        var id: Long = 0,
        var mark: Long = 0,
        var name: String = "Имя из структуры Place",
        var description: String? = null,
        var short_description: String? = null,
        var type: PlaceType = PlaceType(),
        var address: String? = null,
        var latitude: Double = 0.0,
        var longitude: Double = 0.0,
        var photos: BaseCollectionClass<Photo> = BaseCollectionClass(),
        var reviews: BaseCollectionClass<Review> = BaseCollectionClass(),
) {
    companion object Factory {
        private var lastId: Long = -1;

        fun makePlace(): Place {
            lastId++;

            return Place(id = lastId);
        }
    }
}