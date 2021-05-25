package com.solution.citylogia

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.setFragmentResultListener
import com.google.android.gms.location.*
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import com.solution.citylogia.models.BaseCollectionResponse
import com.solution.citylogia.models.PlaceType
import com.solution.citylogia.models.ShortPlace
import com.solution.citylogia.network.RetrofitSingleton.retrofit
import com.solution.citylogia.network.api.IPlaceApi
import com.solution.citylogia.services.MapService
import com.solution.citylogia.utils.getNearest
import com.squareup.picasso.Picasso
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class MapActivity : FragmentActivity(), OnMapReadyCallback {
    private val placeApi = retrofit.create(IPlaceApi::class.java)
    private var mMap: GoogleMap? = null
    private val mapService = MapService()
    private var markers: List<Marker>? = null
    private var userLocationMarker: Marker? = null
    private val ACCESS_LOCATION_REQUEST_CODE = 10001
    private var fusedLocationProviderClient: FusedLocationProviderClient? = null
    private var locationRequest: LocationRequest? = null
    private var refresh = true
    private var geocoder: Geocoder? = null
    private var userLongitude: Double? = null
    private var userLatitude: Double? = null
    private var selectedTyped: ArrayList<PlaceType> = ArrayList()
    private var selectedRadius: Int = 10

    private var zoomLevel = 17.0f //This goes up to 21

    private lateinit var filterPanel: LinearLayout
    private lateinit var menuPanel: LinearLayout
    private lateinit var btnPanel: LinearLayout

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

        filterPanel = findViewById(R.id.maps_tools)
        menuPanel = findViewById(R.id.menu)
        btnPanel = findViewById(R.id.btn_panel)

        var filter = findViewById<Button>(R.id.bt_filter)

        filter.setOnClickListener{
            var bundle = Bundle()
            bundle.putSerializable("selected_types", this.selectedTyped)
            bundle.putSerializable("selected_radius", this.selectedRadius)

            var filtersFragment = FiltersFragment.getNewInstance(bundle);
            supportFragmentManager.beginTransaction()
                    .replace(R.id.map, filtersFragment)
                    .addToBackStack(null)
                    .commit();
        }

        supportFragmentManager.setFragmentResultListener("filters_fragment_apply", this) { _, bundle ->
                    val selectedTypes = bundle.get("selected_types") as ArrayList<PlaceType>?
                    val radius = bundle.get("radius") as Int
                    this.selectedTyped = selectedTyped
                    this.selectedRadius = radius
                    this.loadPlaces(types = selectedTypes, radius = radius.toDouble(), longitude = this.userLongitude, latitude = this.userLatitude)
                }

        val profileBtn: ImageButton = findViewById(R.id.but_profile)
        profileBtn.setOnClickListener { v: View? ->
            startActivity(Intent(this, ProfileActivity::class.java))
            finish()
        }

        val offerBtn: ImageButton = findViewById(R.id.btn_idea)
        offerBtn.setOnClickListener{
            offerPlace()
        }
    }

    override fun onStart() {
        super.onStart()
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            startLocationUpdates()
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
        this.loadPlaces()
        mMap!!.setOnMarkerClickListener { marker: Marker ->
            try {
                val placeId = marker.snippet.toLong()
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

            val btn_zoom_in: ImageButton = findViewById(R.id.btn_zoom_in)
            val btn_zoom_out: ImageButton = findViewById(R.id.btn_zoom_out)
            val btn_navigation: ImageButton = findViewById(R.id.btn_navigation)

            /* Двигает камеру на юзера, но я не знаю куда это вставить, чтобы сразу карта грузилась на юзере
            val latLng = LatLng(locationResult.lastLocation.latitude, locationResult.lastLocation.longitude);
            val point = CameraUpdateFactory.newLatLng(latLng)
            mMap!!.moveCamera(point)*/

            if (mMap != null) {
                setUserLocationMarker(locationResult.lastLocation)

                btn_zoom_in.setOnClickListener {
                    if (zoomLevel + 1.0f <= 21.0f)
                        zoomLevel++;
                    zoomMap(locationResult)
                }

                btn_zoom_out.setOnClickListener {
                    if (zoomLevel - 1.0f >= 0.0f)
                        zoomLevel--;
                    zoomMap(locationResult)
                }

                btn_navigation.setOnClickListener {
                    zoomMap(locationResult)
                }
            }
        }
    }

    private fun zoomMap(locationResult: LocationResult) {
        val cameraPosition: CameraPosition = CameraPosition.Builder()
                .target(LatLng((locationResult.lastLocation.latitude), locationResult.lastLocation.longitude))
                .zoom(zoomLevel)
                .build()
        val cameraUpdate: CameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition)
        mMap!!.animateCamera(cameraUpdate)
    }

    private fun offerPlace() {
        val layoutInflater = LayoutInflater.from(applicationContext)
        val view: View = layoutInflater.inflate(R.layout.layout_offer_place, null)

        val alertDialogBuilder = AlertDialog.Builder(this@MapActivity)
        alertDialogBuilder.setView(view)

        val addressEditText = view.findViewById<EditText>(R.id.offerPlaceAddress)
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("Отправить") { _: DialogInterface?, _: Int ->
                    /*Send data to base*/
                    Toast.makeText(this@MapActivity, "Спасибо! Ваш запрос отправлен!", Toast.LENGTH_LONG).show()}
                .setNegativeButton("Отмена") { _: DialogInterface?, _: Int ->  }

        val alertDialog: AlertDialog = alertDialogBuilder.create()
        alertDialog.show()

        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
            if (TextUtils.isEmpty(addressEditText.text)) {
                Toast.makeText(this@MapActivity, "Напишите адрес", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            } else {
                alertDialog.dismiss()
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {
        fusedLocationProviderClient?.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
    }

    private fun setUserLocationMarker(location: Location) {
        this.userLongitude = location.longitude
        this.userLatitude = location.latitude

        val latLng = LatLng(location.latitude, location.longitude)

        if (userLocationMarker == null) {
            val markerOptions = MarkerOptions()
            markerOptions.position(latLng)
            markerOptions.icon(this.bitmapDescriptorFromVector(this, R.drawable.ic_my_navigation))
            userLocationMarker = mMap!!.addMarker(markerOptions)
            if (this.refresh) {
                mMap!!.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoomLevel))
                this.refresh = false
            }
        } else {
            userLocationMarker!!.position = latLng
            if (this.refresh) {
                mMap!!.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoomLevel))
                this.refresh = false
            }
        }
    }

    private fun loadNearestPlace(place: ShortPlace) {
        val nearPlaceLayout = this.findViewById(R.id.maps_tools) as LinearLayout
        val cricketerView = layoutInflater.inflate(R.layout.near_place, null, false)

        cricketerView.findViewById<TextView>(R.id.place_name).text = place.name
        cricketerView.findViewById<TextView>(R.id.place_address).text = place.address
        var nearPlaceImage = cricketerView.findViewById<ImageView>(R.id.near_place_img)
        Picasso.get().load(R.drawable.near_place_template)
                     .into(nearPlaceImage)
        nearPlaceLayout.addView(cricketerView);
    }

    private fun bitmapDescriptorFromVector(context: Context, vectorResId: Int): BitmapDescriptor? {
        val vectorDrawable = ContextCompat.getDrawable(context, vectorResId)
        vectorDrawable!!.setBounds(0, 0, vectorDrawable.intrinsicWidth, vectorDrawable.intrinsicHeight)
        val bitmap = Bitmap.createBitmap(vectorDrawable.intrinsicWidth, vectorDrawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        vectorDrawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }

    private fun clearMarkers() {
        this.markers?.forEach {
            it.remove();
        }
    }

    @SuppressLint("CheckResult")
    private fun loadPlaces(types: ArrayList<PlaceType>? = null, latitude: Double? = null, radius: Double? = null, longitude: Double? = null){
        val typeIds = types?.map { type -> type.id }

        this.placeApi.getAllPlaces(latitude = latitude, longitude = longitude, radius = radius, typeIds = typeIds)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe({ places: BaseCollectionResponse<ShortPlace>? ->

                    val places = places?.data?.elements
                    if (places != null) {
                        this.clearMarkers()
                        var nearest = places.getNearest()
                        if (nearest != null)
                            this.loadNearestPlace(nearest);
                        this.markers = this.mapService.drawMarkers(this.mMap, places)
                    }
                }, {})
    }
}