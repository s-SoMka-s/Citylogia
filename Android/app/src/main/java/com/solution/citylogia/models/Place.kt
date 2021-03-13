package com.solution.citylogia.models

data class Place(
        val id: Long = 0,
        val mark: Long = 0,
        val name: String = "",
        val description: String? = null,
        val type: PlaceType = PlaceType(),
        val address: PlaceAddress = PlaceAddress(),
        val photos: BaseCollectionClass<Photo> = BaseCollectionClass(),
        val reviews: BaseCollectionClass<Review> = BaseCollectionClass(),

)
