package com.solution.citylogia;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.solution.citylogia.models.ShortPlace;
import com.solution.citylogia.network.RetrofitSingleton;
import com.solution.citylogia.network.api.IPlaceApi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback {
    private Iterable<ShortPlace> places;
    private ArrayList<Place> allPlaces;
    private static boolean refresh = true;
    private static boolean requestToGetPlaces = true; //временное
    private static final String Tag = "MainActivity";
    private GoogleMap mMap;
    private Geocoder geocoder;
    private IPlaceApi placeApi;
    private Place placeInfo = null;
    private int ACCESS_LOCATION_REQUEST_CODE = 10001;
    FusedLocationProviderClient fusedLocationProviderClient;
    LocationRequest locationRequest;
    //new
    Button filter;
    String[] typeArray = {"Парки", "Архитектура", "Еда", "Другое"};
    private boolean[] selectedType = new boolean[typeArray.length];
    ArrayList<Integer> typeList = new ArrayList<>();
    ArrayList<Marker> markers = new ArrayList<>();

    Marker userLocationMarker;

    //new
    private MaterialSearchBar materialSearchBar;
    private List<AutocompletePrediction> predictionList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String languageToLoad = "ru";
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);

        Configuration config = new Configuration();
        config.setLocale(locale);
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());

        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        geocoder = new Geocoder(this);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        locationRequest = LocationRequest.create();
        locationRequest.setInterval(500);
        locationRequest.setFastestInterval(500);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        /////////////////new

        for (int i = 0; i < typeArray.length; i++) {
            selectedType[i] = false;
        }
        filter = findViewById(R.id.bt_filter);
        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Какие места вам интересны?");
                builder.setCancelable(false);
                builder.setMultiChoiceItems(typeArray, selectedType, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {

                    }
                });

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        refreshInterestingPlaces(); //new
                    }
                });

                builder.setNeutralButton("Выбрать всё", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        for (int i = 0; i < selectedType.length; i++) {
                            selectedType[i] = true;
                            typeList.add(which);
                        }
                        refreshInterestingPlaces();//new
                        //                      Collections.sort(typeList);
                    }
                });

                builder.setNegativeButton("Очистить всё", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        for (int i = 0; i < selectedType.length; i++) {
                            selectedType[i] = false;
                            typeList.clear();
                        }
                        refreshInterestingPlaces();//new
                    }
                });

                builder.show();
            }
        });

        materialSearchBar = findViewById(R.id.searchBar);
        materialSearchBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, Search.class);
                i.putExtra("all places", allPlaces);
                i.putExtra("selected types", selectedType);
                LatLng position = userLocationMarker.getPosition();
                i.putExtra("user position", position);
                startActivityForResult(i, 2404);
                //startActivity(i);
            }
        });
    }

    private void drawMarkers(Iterable<ShortPlace> placesToDraw) {
        placesToDraw.forEach(place -> {
            LatLng latLng = new LatLng(place.getLatitude(), place.getLongitude());
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.snippet("" + place.getId());
            mMap.addMarker(markerOptions);
        });
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        googleMap.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                        this, R.raw.mapstyle));


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, ACCESS_LOCATION_REQUEST_CODE);
        }

        Retrofit retrofit = RetrofitSingleton.INSTANCE.getRetrofit();
        this.placeApi = retrofit.create(IPlaceApi.class);

        this.placeApi.getAllPlaces().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(places -> {
            this.places = places.getData().getElements();
            System.out.println(this.places);
            this.drawMarkers(this.places);
        });

        mMap.setOnMarkerClickListener(marker -> {
            Long placeId = Long.parseLong(marker.getSnippet());
            System.out.println("ID этого места " + placeId);
            Intent i = new Intent(MainActivity.this, PlaceInside.class);
            startActivity(i);

            return false;
        });
    }

    LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(@NonNull LocationResult locationResult) {
            super.onLocationResult(locationResult);
            Log.d(Tag, "onLocationResult: " + locationResult.getLastLocation());
            if (mMap != null) {
                setUserLocationMarker(locationResult.getLastLocation());
            }
        }
    };

    private void setUserLocationMarker(Location location) {
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        if (userLocationMarker == null) {
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            //markerOptions.icon(this.descriptor.bitmapDescriptorFromVector(getApplicationContext(), R.drawable.ic_baseline_my_location_24));
            userLocationMarker = mMap.addMarker(markerOptions);
            if (refresh) {
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17));
                refresh = false;
            }
        } else {
            userLocationMarker.setPosition(latLng);
            if (refresh) {
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17));
                refresh = false;
            }
        }
    }

    @SuppressLint("MissingPermission")
    private void startLocationUpdates() {
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
    }

    private void stopLocationUpdates() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            startLocationUpdates();
        } else {

        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        //stopLocationUpdates();
    }

    @SuppressLint("MissingPermission")
    private void enableUserLocation() {
        mMap.setMyLocationEnabled(true);
    }

    private void zoomToUserLocation() {
        @SuppressLint("MissingPermission") Task<Location> locationTask = fusedLocationProviderClient.getLastLocation();
        locationTask.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 20));
            }
        });
    }

    private void createInterestingPlaces() { // new placesFound и placesDraw - разные вещи, ибо нам мб нужно будет показать только парки или т.п.
        MarkerOptions markerOptions = new MarkerOptions();
        Marker newMarker;

        for (int i = 0; i < allPlaces.size(); i++) {
            //markerOptions.position(new LatLng(allPlaces.get(i).getAddress().latitude, allPlaces.get(i).address.longitude));
            //markerOptions.icon(bitmapDescriptorFromVector(getApplicationContext(), R.drawable.ic_baseline_place_36));
            //markerOptions.snippet(Integer.toString(allPlaces.get(i).id - 1)); // я в сниппет сую АЙДИ этого места, чтобы инфу о нем можно было передавать в дургие активитис. Внимание -1, так id с 1, массив с 0
            newMarker = mMap.addMarker(markerOptions);
            markers.add(newMarker);
            newMarker.setVisible(false);
        }

        refreshInterestingPlaces(); //new
    }

    private void refreshInterestingPlaces() { //new

    }

    public void putMarker(MarkerOptions markerOptions, LatLng coords, long placeId) {
        markerOptions.position(coords);
        //markerOptions.icon(descriptor.bitmapDescriptorFromVector(getApplicationContext(), R.drawable.ic_baseline_place_36));
        markerOptions.snippet(java.lang.Long.toString(placeId));
        mMap.addMarker(markerOptions);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode == 2404) {
            if(data != null) {
                int index = data.getIntExtra("selected places in search",-1);
                LatLng latLng = markers.get(index).getPosition();
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17));
                //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17));
            }
        }
    }

}