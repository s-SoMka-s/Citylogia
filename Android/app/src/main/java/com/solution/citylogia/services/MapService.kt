package com.solution.citylogia.services

import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.solution.citylogia.R
import com.solution.citylogia.models.ShortPlace

class MapService {
    fun drawMarkers(mMap: GoogleMap? = null, placesToDraw: Iterable<ShortPlace>): List<Marker> {
        return placesToDraw.map { (id, _, latitude, longitude) ->
           val latLng = LatLng(latitude, longitude)
           val markerOptions = createMarker(latLng, id)
           mMap!!.addMarker(markerOptions)
       }
    }

    private fun createMarker(coords: LatLng, placeId: Long): MarkerOptions? {
        val markerOptions = MarkerOptions()
        markerOptions.position(coords);
        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker))
        markerOptions.snippet(placeId.toString())
        return markerOptions
    }
}