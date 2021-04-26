package com.solution.citylogia

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.os.Looper
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.solution.citylogia.models.BaseCollectionResponse
import com.solution.citylogia.models.ShortPlace
import com.solution.citylogia.network.RetrofitSingleton.retrofit
import com.solution.citylogia.network.api.IPlaceApi
import com.solution.citylogia.services.MapService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.*


class MapActivity : FragmentActivity(), OnMapReadyCallback {
    private val placeApi = retrofit.create(IPlaceApi::class.java)
    private var mMap: GoogleMap? = null
    private val mapService = MapService()
    private var userLocationMarker: Marker? = null
    private val ACCESS_LOCATION_REQUEST_CODE = 10001
    private var fusedLocationProviderClient: FusedLocationProviderClient? = null
    private var locationRequest: LocationRequest? = null
    private var refresh = true
    private var geocoder: Geocoder? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        this.geocoder = Geocoder(this)
        this.locationRequest = LocationRequest.create()
        this.fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        this.locationRequest?.interval = 500
        this.locationRequest?.fastestInterval = 500
        locationRequest?.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }

    override fun onStart() {
        super.onStart()
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            startLocationUpdates()
        } else {
        }
    }

    @SuppressLint("CheckResult")
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        googleMap.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                        this, R.raw.mapstyle))
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), ACCESS_LOCATION_REQUEST_CODE)
        }
        this.placeApi.getAllPlaces()
                     .subscribeOn(Schedulers.io())
                     .observeOn(AndroidSchedulers.mainThread()).subscribe({ places: BaseCollectionResponse<ShortPlace>? ->
                        val places = places?.data?.elements
                        if (places != null) {
                            this.mapService.drawMarkers(this.mMap, places)
                        }
                    }, {})
        mMap!!.setOnMarkerClickListener { marker: Marker ->
            try {
                val placeId = marker.snippet.toLong()
                println("ID этого места $placeId")
                val i = Intent(this@MapActivity, PlaceInside::class.java)
                i.putExtra("id", placeId)
                startActivity(i)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            false
        }
    }

    private var locationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            super.onLocationResult(locationResult)
            if (mMap != null) {
                setUserLocationMarker(locationResult.lastLocation)
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {
        fusedLocationProviderClient?.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
    }

    private fun setUserLocationMarker(location: Location) {
        val latLng = LatLng(location.latitude, location.longitude)
        if (userLocationMarker == null) {
            val markerOptions = MarkerOptions()
            markerOptions.position(latLng)
            markerOptions.icon(this.bitmapDescriptorFromVector(this, R.drawable.ic_baseline_my_location_24))
            userLocationMarker = mMap!!.addMarker(markerOptions)
            if (this.refresh) {
                mMap!!.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17f))
                this.refresh = false
            }
        } else {
            userLocationMarker!!.position = latLng
            if (this.refresh) {
                mMap!!.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17f))
                this.refresh = false
            }
        }
    }

    private fun bitmapDescriptorFromVector(context: Context, vectorResId: Int): BitmapDescriptor? {
        val vectorDrawable = ContextCompat.getDrawable(context, vectorResId)
        vectorDrawable!!.setBounds(0, 0, vectorDrawable.intrinsicWidth, vectorDrawable.intrinsicHeight)
        val bitmap = Bitmap.createBitmap(vectorDrawable.intrinsicWidth, vectorDrawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        vectorDrawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }
}