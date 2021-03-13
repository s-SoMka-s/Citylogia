package com.solution.citylogia

import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.solution.citylogia.models.Place
import com.solution.citylogia.utils.Descriptor

class MapService {
    private val descriptor: Descriptor = Descriptor()
    private var mMap: GoogleMap

    constructor(mMap: GoogleMap) {
        this.mMap = mMap
    }

    fun drawInterestingPlaces(places: List<Place>) {
        val markerOptions = MarkerOptions()

        places.forEach { (id, _, _, _, _, address) ->
            val coords = LatLng(address.latitude, address.longitude)
            //putMarker(markerOptions, coords, id)
        }
    }
    /*
    private fun putMarker(markerOptions: MarkerOptions, coords: LatLng, placeId: Long) {
        markerOptions.position(coords)
        markerOptions.icon(this.descriptor.bitmapDescriptorFromVector(getApplicationContext(), R.drawable.ic_baseline_place_36))
        markerOptions.snippet(java.lang.Long.toString(placeId)) // я в сниппет сую АЙДИ этого места, чтобы инфу о нем можно было передавать в дургие активитис. Внимание -1, так id с 1, массив с 0 null
        this.mMap.addMarker(markerOptions)
    }
*/
}