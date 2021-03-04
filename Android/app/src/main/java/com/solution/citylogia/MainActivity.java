package com.solution.citylogia;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.mancj.materialsearchbar.MaterialSearchBar;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import dagger.Lazy;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback {

    private Place[] places;
    private static boolean refresh = true;
    private static boolean requestToGetPlaces = true; //временное
    private static final String Tag = "MainActivity";
    private GoogleMap mMap;
    private Geocoder geocoder;
    private int ACCESS_LOCATION_REQUEST_CODE = 10001;
    FusedLocationProviderClient fusedLocationProviderClient;
    LocationRequest locationRequest;

    Marker userLocationMarker;

    /**
     * Класс место, который содержит всё описание о нём: тип, отзывы, фото и т.п.
     * Используется для отображения меток на карте и для перехода на страницу с отзывами/оценками.
     * Костыль - айдишник места (элемент массива) вписывается как snippet, который на карте без title
     * не отображается. Т.о. я могу передать Place[i] в активитис с оценками и отзывами
     */

    private static class Place implements Serializable {

        static class Type implements Serializable {
            int id;
            String name;

            /**
             * @param id   айди типа места
             * @param name имя типа места
             */
            private Type(int id, String name) {
                this.id = id;
                this.name = name;
            }
        }

        static class Address implements Serializable {
            Double latitude;
            Double longitude;
            //LatLng latLng;
            String country;
            String province;
            String city;
            String district;
            String street;
            String house;
            String flat;
            int postcode;

            /**
             * @param country  Страна
             * @param province Регион
             * @param city     Город
             * @param district Район
             * @param street   Улица
             * @param house    Дом
             * @param flat     Квартира
             * @param postcode Почтовый индекс
             */
            private Address(Double latitude, Double longitude, String country, String province,
                            String city, String district, String street, String house, String flat,
                            int postcode) {
                this.latitude = latitude;
                this.longitude = longitude;
                this.country = country;
                this.province = province;
                this.city = city;
                this.district = district;
                this.street = street;
                this.house = house;
                this.flat = flat;
                this.postcode = postcode;
            }
        }

        static class Photos {
            int id;
            String link;

            private Photos(int id, String link) {
                this.id = id;
                this.link = link;
            }
        }

        static class Reviews {
            int id;
            String body;
            double mark;
            Date published_at;

            static class Author  {
                int id;
                String name;
                String surname;
                int mark;

                /**
                 * @param id      айди автора
                 * @param name    имя автора
                 * @param surname фамилия автора
                 * @param mark    оценка автора
                 */
                private Author(int id, String name, String surname, int mark) {
                    this.id = id;
                    this.name = name;
                    this.surname = surname;
                    this.mark = mark;
                }
            }

            /**
             * @param id           айди обзора
             * @param body         текст обзора
             * @param mark         оценка обзора
             * @param published_at дата публикации
             */
            private Reviews(int id, String body, double mark, Date published_at) {
                this.id = id;
                this.body = body;
                this.mark = mark;
                this.published_at = published_at;
            }

            ArrayList<Author> author = new ArrayList<>();
        }

        int id; //id места
        String name; // имя места
        Type type;
        Address address;
        String description;
        ArrayList<Photos> photos = new ArrayList<>();
        int ReviewsCount;
        ArrayList<Reviews> reviews = new ArrayList<>();

        /**
         * @param id      айди места
         * @param type    тип места
         * @param name    имя места
         * @param address адрес места
         */
        private Place(int id, Type type, String name, Address address, String description) {
            this.id = id;
            this.type = type;
            this.name = name;
            this.address = address;
            this.description = description;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String languageToLoad = "ru";
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
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

        //  mMap.setOnMapLongClickListener(this);
        //  mMap.setOnMarkerDragListener(this);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, ACCESS_LOCATION_REQUEST_CODE);
        }

        parseInterestingPlaces();

        /*
         * Я тут при клике на маркер перехожу на активити с его описанием.
         */
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                String PlaceID = marker.getSnippet();

                Intent i = new Intent(MainActivity.this, Test.class); // АНДРЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЙ 2й параметр
                i.putExtra("places element", places[Integer.parseInt(PlaceID)]); // контекст - вся инфа о месте - изу структуру!
            /*    i.putExtra("id", places[Integer.parseInt(PlaceID)].id);
                i.putExtra("name", places[Integer.parseInt(PlaceID)].name);
                i.putExtra("type", places[Integer.parseInt(PlaceID)].type);
                i.putExtra("photos", places[Integer.parseInt(PlaceID)].photos);
                i.putExtra("description", places[Integer.parseInt(PlaceID)].description);
                i.putExtra("reviews count", places[Integer.parseInt(PlaceID)].ReviewsCount);
                i.putExtra("reviews", places[Integer.parseInt(PlaceID)].reviews);
                i.putExtra("address", places[Integer.parseInt(PlaceID)].address);*/
                startActivity(i);

                return false;
            }
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
            markerOptions.icon(bitmapDescriptorFromVector(getApplicationContext(), R.drawable.ic_baseline_my_location_24));
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
        stopLocationUpdates();
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

    private void parseInterestingPlaces() {
        int count = 2; //поле в JSON, сколько мест в базе всего

        places = new Place[count];

        places[0] = new Place(1, new Place.Type(1, "Архитектура"), "Театр оперы и балета",
                new Place.Address(55.030443, 82.925023, "Россия",
                        "Новосибирская область", "Новосибирск", "Центральный район",
                        "Красный проспект", "", "", 433333), "у мэрии");

        places[1] = new Place(2, new Place.Type(1, "Парки"), "Нарымский сквер",
                new Place.Address(55.043548, 82.909349, "Россия",
                        "Новосибирская область", "Новосибирск", "Центральный район",
                        "Челюскинцев", "", "", 630099), "у цирка");


        List<Place> list = Arrays.asList(places);

        drawInterestingPlaces(list);
    }

    private void drawInterestingPlaces(List<Place> placesToDraw) { // placesFound и placesDraw - разные вещи, ибо нам мб нужно будет показать только парки или т.п.
        MarkerOptions markerOptions = new MarkerOptions();

        for (int i = 0; i < placesToDraw.size(); i++) {
            markerOptions.position(new LatLng(placesToDraw.get(i).address.latitude, placesToDraw.get(i).address.longitude));
            markerOptions.icon(bitmapDescriptorFromVector(getApplicationContext(), R.drawable.ic_baseline_place_36));
            markerOptions.snippet(Integer.toString(placesToDraw.get(i).id-1)); // я в сниппет сую АЙДИ этого места, чтобы инфу о нем можно было передавать в дургие активитис. Внимание -1, так id с 1, массив с 0
            mMap.addMarker(markerOptions);
        }

    }

    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }


}