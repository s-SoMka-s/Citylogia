package com.solution.citylogia.utils

import com.solution.citylogia.models.Place
import com.solution.citylogia.models.PlaceType
import com.solution.citylogia.models.Review
import com.solution.citylogia.models.ShortPlace

class Generator {
    fun genShortPlaces(count: Long): ArrayList<ShortPlace> {
        var res = ArrayList<ShortPlace>();
        for (i in 1..count) {
            res.add(ShortPlace.makeShortPlace());
        }

        return res;
    }

    fun genPlaces(count: Long): ArrayList<Place> {
        var res = ArrayList<Place>();
        for (i in 1..count) {
            res.add(Place.makePlace());
        }

        return res;
    }

    fun genReviews(count: Long): ArrayList<Review> {
        var res = ArrayList<Review>()
        for(i in 1..count) {
            res.add(Review.makeReview());
        }

        return res;
    }

    fun genPlaceTypes(count: Long): ArrayList<PlaceType> {
        var res = ArrayList<PlaceType>()
        for(i in 1..count) {
            res.add(PlaceType.makeType())
        }

        return res;
    }
}