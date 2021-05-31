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
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.setFragmentResultListener
import com.google.android.gms.location.*
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.mancj.materialsearchbar.MaterialSearchBar
import com.solution.citylogia.models.BaseCollectionResponse
import com.solution.citylogia.models.Place
import com.solution.citylogia.models.PlaceType
import com.solution.citylogia.models.ShortPlace
import com.solution.citylogia.network.RetrofitSingleton
import com.solution.citylogia.network.api.IPlaceApi
import com.solution.citylogia.services.AuthorizationService
import com.solution.citylogia.services.MapService
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject


@AndroidEntryPoint
class MapActivity : FragmentActivity(), OnMapReadyCallback {
    lateinit var placeApi: IPlaceApi
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
    private var allTyped: ArrayList<PlaceType> = ArrayList()
    private var selectedRadius: Int = 10
    private var selectedPlaces: List<Place>? = null
    private var allPlaces: List<Place>? = null
    private var flag: Boolean = true

    private var zoomLevel = 17.0f //This goes up to 21

    private lateinit var filterPanel: LinearLayout
    private lateinit var menuPanel: LinearLayout
    private lateinit var btnPanel: LinearLayout

    @Inject
    lateinit var retrofitNew: RetrofitSingleton

    @Inject
    lateinit var authService: AuthorizationService


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        this.placeApi = retrofitNew.retrofit.create(IPlaceApi::class.java)

        this.loadTypes()

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

        filter.setOnClickListener {
            this.startFilters()
        }

        supportFragmentManager.setFragmentResultListener("filters_fragment_apply", this) { _, bundle ->
            this.getFiltersResult(bundle)
        }

        val profileBtn: ImageButton = findViewById(R.id.but_profile)
        profileBtn.setOnClickListener {
            if (authService.isLoggedIn()) {
                startActivity(Intent(this, ProfileActivity::class.java))
            } else {
                startActivity(Intent(this, LoginActivity::class.java))
            }
        }

        val offerBtn: ImageButton = findViewById(R.id.btn_idea)
        offerBtn.setOnClickListener {
            if (authService.isLoggedIn()) {
                offerPlace()
            } else {
                Toast.makeText(this, "Ошибка, создайте/войдите\n в аккаунт!", Toast.LENGTH_LONG).show()
            }
        }

        ////new
        //var filter: Button? = null
        var materialSearchBar: MaterialSearchBar? = null

        //filter = findViewById(R.id.bt_filter)
        materialSearchBar = findViewById<MaterialSearchBar>(R.id.searchBar)
        materialSearchBar.setOnClickListener(View.OnClickListener { v: View? ->
            val i = Intent(this@MapActivity, Search::class.java)
            val gson = GsonBuilder().setPrettyPrinting().create()

            val selectedTypes: ArrayList<Boolean> = ArrayList()
            for (i in 0 until allTyped.size) {
                selectedTypes.add(false)
            }

            for (i in 0 until selectedTyped.size) {
                //костыль
                selectedTypes[selectedTyped[i].id.toInt() - 7] = true
            }

            var markerIds: ArrayList<Long> = ArrayList()
            for (i in 0 until markers!!.size) {
                //костыль
                markerIds.add(markers!![i].snippet.toLong())
            }

            var selectedTypedX: ArrayList<PlaceType> = ArrayList()
            for (i in 0 until selectedPlaces!!.size) {
                var flag = true
                for (j in 0 until selectedTypedX.size) {
                    if (selectedTypedX[j].id == selectedPlaces!![i].type.id) {
                        flag = false
                    }
                }
                if (flag) {
                    selectedTypedX.add(selectedPlaces!![i].type)
                }
            }

            //this.selectedTyped.add(PlaceType())
            i.putExtra("all places", gson.toJson(this.selectedPlaces))
            i.putExtra("all types", gson.toJson(this.allTyped))
            i.putExtra("selected types", gson.toJson(selectedTypedX))
            i.putExtra("markers", gson.toJson(markerIds))
            /*val args = Bundle()
            args.putSerializable("selected_types", this.mar)
            i.putExtra("bundle", args);*/

            //i.putExtra("selected types", selectedTypes)
            i.putExtra("selected radii", selectedRadius)
            i.putExtra("user latitude", userLatitude)
            i.putExtra("user longitude", userLongitude)
            //startActivity(i)

            //getSupportActionBar().hide();
            startActivityForResult(i, 2404)
            //finish()
            /*try {
                //val placeId = marker.snippet.toLong()
                val i = Intent(this@MapActivity, PlaceInside::class.java)
                i.putExtra("id", 20)
                startActivity(i)
            } catch (e: Exception) {
                e.printStackTrace()
            }*/


        })

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        var result: String = ""
        try {
            super.onActivityResult(requestCode, resultCode, data)
            if (requestCode === 2404 && resultCode === RESULT_OK) {
                if (data != null) {
                    result = data.getStringExtra("result").toString()
                    val placeId = markers!![result.toInt()].snippet.toLong()
                    val i = Intent(this@MapActivity, PlaceInside::class.java)
                    i.putExtra("id", placeId)
                    startActivity(i)
                }
            }
        } catch (ex: java.lang.Exception) {
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
                i.getSerializableExtra("place_types")
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
                    Toast.makeText(this@MapActivity, "Спасибо! Ваш запрос отправлен!", Toast.LENGTH_LONG).show()
                }
                .setNegativeButton("Отмена") { _: DialogInterface?, _: Int -> }

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
                mMap!!.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoomLevel))
                this.refresh = false
            }
        } else {
            userLocationMarker!!.position = latLng
            if (this.refresh) {
                mMap!!.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoomLevel))
                this.refresh = false
            }
        }
    }

    private fun loadNearestPlace(place: ShortPlace) {
        val nearPlaceLayout = this.findViewById(R.id.maps_tools) as LinearLayout
        val cricketerView = layoutInflater.inflate(R.layout.near_place, null, false)

        cricketerView.findViewById<TextView>(R.id.place_name).text = place.name
        cricketerView.findViewById<TextView>(R.id.place_address).text = place.city
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
    private fun loadPlaces(types: ArrayList<PlaceType>? = null, latitude: Double? = null, radius: Double? = null, longitude: Double? = null) {
        val typeIds = types?.map { type -> type.id }

        this.placeApi.getAllPlaces(latitude = latitude, longitude = longitude, radius = radius, typeIds = typeIds)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe({ places: BaseCollectionResponse<Place>? ->

                    val places = places?.data?.elements as ArrayList<Place>
                    if (places != null) {
                        if (flag) {
                            allPlaces = places.clone() as ArrayList<Place>
                            flag = false
                        }
                        this.clearMarkers()
                        /*for (String item : rOriginalTitle) {
                if (item.toLowerCase().contains(constraint)) {
                    results.add(item);
                }
              }*/
                        /*for (String item : rOriginalTitle) {
                if (item.toLowerCase().contains(constraint)) {
                    results.add(item);
                }
            }*/
                        val xx = java.util.ArrayList<Place>()
                        for (i in 0 until places.size) {
                            var count = 0
                            if (distanceBetweenTwoMarkers(places[i]) <= selectedRadius*1000) {
                                xx.add(places[i])
                                count++
                            }
                        }
                        selectedPlaces = xx.toList()
                        this.markers = this.mapService.drawMarkers(this.mMap, xx)
                    }
                }, {})
    }

    @SuppressLint("CheckResult")
    private fun loadTypes() {
        this.placeApi.getPlaceTypes()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { types: BaseCollectionResponse<PlaceType>? ->
                    val a = types?.data?.elements as ArrayList<PlaceType>
                    this.selectedTyped = a
                    this.allTyped = a.clone() as ArrayList<PlaceType>
                };
    }

    private fun startFilters() {
        var bundle = Bundle()
        val gson = GsonBuilder().setPrettyPrinting().create()

        bundle.putString("selected_types", gson.toJson(this.selectedTyped))
        bundle.putInt("selected_radius", this.selectedRadius)

        var filtersFragment = FiltersFragment.getNewInstance(bundle);
        supportFragmentManager.beginTransaction()
                .replace(R.id.map, filtersFragment)
                .addToBackStack(null)
                .commit();
    }

    private fun getFiltersResult(bundle: Bundle) {
        val gson = GsonBuilder().setPrettyPrinting().create()
        var jsonedTypes = bundle.getString("selected_types");
        val placeTypesType = object : TypeToken<java.util.ArrayList<PlaceType?>?>() {}.type

        val selectedTypes = gson.fromJson<ArrayList<PlaceType>>(jsonedTypes, placeTypesType)
        val radius = bundle.getInt("selected_radius")

        this.selectedTyped = selectedTyped
        this.selectedRadius = radius
        this.loadPlaces(types = selectedTypes, radius = radius.toDouble(), longitude = this.userLongitude, latitude = this.userLatitude)
    }

    private fun distanceBetweenTwoMarkers(place: Place): Float {
        val results = FloatArray(1)
        Location.distanceBetween(userLatitude!!, userLongitude!!,
                place.latitude, place.longitude, results)
        return results[0]
    }
}

