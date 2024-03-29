package com.solution.citylogia.services

import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.solution.citylogia.R
import com.solution.citylogia.models.Place

class MapService {
    fun drawMarkers(mMap: GoogleMap? = null, placesToDraw: ArrayList<Place>): List<Marker> {
        return placesToDraw.map {
           val latLng = LatLng(it.latitude, it.longitude)
           val markerOptions = createMarker(latLng, it.id)
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