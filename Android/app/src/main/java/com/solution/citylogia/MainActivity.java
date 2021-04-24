package com.solution.citylogia;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
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
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.Place;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.solution.citylogia.models.PlaceType;
import com.solution.citylogia.models.ShortPlace;
import com.solution.citylogia.network.RetrofitSingleton;
import com.solution.citylogia.network.api.IPlaceApi;
import com.solution.citylogia.services.MapService;
import com.solution.citylogia.utils.Generator;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback {
    private Iterable<ShortPlace> places;
    private final Generator generator = new Generator();
    private final MapService mapService = new MapService();
    private static boolean refresh = true;
    private static final String Tag = "MainActivity";
    private GoogleMap mMap;
    private Geocoder geocoder;
    private IPlaceApi placeApi;
    private Place placeInfo = null;
    private int ACCESS_LOCATION_REQUEST_CODE = 10001;
    FusedLocationProviderClient fusedLocationProviderClient;
    LocationRequest locationRequest;

    Button filter;
    ArrayList<PlaceType> typeArray = this.generator.genPlaceTypes(4);
    private ArrayList<PlaceType> selectedTypes = new ArrayList<>();
    ArrayList<Marker> markers = new ArrayList<>();

    Marker userLocationMarker;

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
        filter = findViewById(R.id.bt_filter);

        filter.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Какие места вам интересны?");
            builder.setCancelable(false);
            builder.setMultiChoiceItems(this.prepareTypesForFilter(), this.prepareSelectedTypesForFilter(), new DialogInterface.OnMultiChoiceClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which, boolean isChecked) {

                }
            });

            builder.setPositiveButton("OK", (dialog, which) -> {
               // refreshInterestingPlaces(); //new
            });

            builder.setNeutralButton("Выбрать всё", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    selectedTypes.addAll(typeArray);
                    //refreshInterestingPlaces();//new
                    //                      Collections.sort(typeList);
                }
            });

            builder.setNegativeButton("Очистить всё", (dialog, which) -> {
                selectedTypes.clear();
                System.out.println(selectedTypes);
                //refreshInterestingPlaces();//new
            });

            builder.show();
        });

        materialSearchBar = findViewById(R.id.searchBar);
        materialSearchBar.setOnClickListener(v -> {
            Intent i = new Intent(MainActivity.this, Search.class);
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            i.putExtra("places", gson.toJson(this.places));
            i.putExtra("selected types", selectedTypes);
            LatLng position = userLocationMarker.getPosition();
            i.putExtra("user position", position);
            startActivityForResult(i, 2404);

        });
    }

    private void drawMarkers(Iterable<ShortPlace> placesToDraw) {
        placesToDraw.forEach(place -> {
            LatLng latLng = new LatLng(place.getLatitude(), place.getLongitude());
            MarkerOptions markerOptions = this.createMarker(latLng, place.getId());
            mMap.addMarker(markerOptions);
        });
    }

    private MarkerOptions createMarker(LatLng coords, long placeId) {
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(coords);
        markerOptions.icon(this.bitmapDescriptorFromVector(getApplicationContext(), R.drawable.ic_baseline_place_36));
        markerOptions.snippet(Long.toString(placeId));
        return markerOptions;
    }

    private String[] prepareTypesForFilter()
    {
        String[] res = new String[this.typeArray.size()];
        return this.typeArray
                   .stream()
                   .map(t -> t.getName())
                   .collect(Collectors.toList())
                   .toArray(res);
    }

    private boolean[] prepareSelectedTypesForFilter()
    {
        boolean[] res = new boolean[this.typeArray.size()];

        int i = 0;
        for (PlaceType type: this.typeArray) {
            if (this.selectedTypes.contains(type))
                res[i] = true;
            else
                res[i] = false;
            i++;
        }

        return res;
    }

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
            this.mapService.drawMarkers(this.mMap, this.places);
        });

        mMap.setOnMarkerClickListener(marker -> {
            try {
                Long placeId = Long.parseLong(marker.getSnippet());
                System.out.println("ID этого места " + placeId);
                Intent i = new Intent(MainActivity.this, PlaceInside.class);
                startActivity(i);
            }
            catch (Exception e){
                e.printStackTrace();
            }
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

    public BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    private void setUserLocationMarker(Location location) {
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        if (userLocationMarker == null) {
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.icon(this.bitmapDescriptorFromVector(getApplicationContext(), R.drawable.ic_baseline_my_location_24));
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
    }

    @SuppressLint("MissingPermission")
    private void enableUserLocation() {
        mMap.setMyLocationEnabled(true);
    }

    private void zoomToUserLocation() {
        @SuppressLint("MissingPermission") Task<Location> locationTask = fusedLocationProviderClient.getLastLocation();
        locationTask.addOnSuccessListener(location -> {
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 20));
        });
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode == 2404) {
            if(data != null) {
                int index = data.getIntExtra("selected places in search",-1);
                LatLng latLng = markers.get(index).getPosition();
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17));
            }
        }
    }

}