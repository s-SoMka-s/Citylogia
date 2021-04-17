package com.solution.citylogia;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

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
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Marker;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.mancj.materialsearchbar.MaterialSearchBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;

import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback {
    private Place[] places;
    private ArrayList<Place> allPlaces;
    private ArrayList<Place> placesToShow; //new
    private static boolean refresh = true;
    private static boolean requestToGetPlaces = true; //временное
    private static final String Tag = "MainActivity";
    private GoogleMap mMap;
    private Geocoder geocoder;
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
    private PlacesClient placesClient;
    private List<AutocompletePrediction> predictionList;

    /**
     * Класс место, который содержит всё описание о нём: тип, отзывы, фото и т.п.
     * Используется для отображения меток на карте и для перехода на страницу с отзывами/оценками.
     * Костыль - айдишник места (элемент массива) вписывается как snippet, который на карте без title
     * не отображается. Т.о. я могу передать Place[i] в активитис с оценками и отзывами
     */
    public static class Place implements Parcelable {

        protected Place(Parcel in) {
            id = in.readInt();
            name = in.readString();
            mark = in.readDouble();
            description = in.readString();
            reviewsCount = in.readInt();
            isVisible = in.readByte() != 0;
            type = in.readParcelable(Type.class.getClassLoader());
            address = in.readParcelable(Address.class.getClassLoader());
        }

        public static final Creator<Place> CREATOR = new Creator<Place>() {
            @Override
            public Place createFromParcel(Parcel in) {
                return new Place(in);
            }

            @Override
            public Place[] newArray(int size) {
                return new Place[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(id);
            dest.writeString(name);
            dest.writeDouble(mark);
            dest.writeString(description);
            dest.writeInt(reviewsCount);
            dest.writeByte((byte) (isVisible ? 1 : 0));
            dest.writeParcelable(type, flags);
            dest.writeParcelable(address, flags);
        }

        static class Type implements Parcelable {
            int id;
            String name;

            /**
             * @param id   айди типа места
             * @param name имя типа места
             */
            Type(int id, String name) {
                this.id = id;
                this.name = name;
            }

            protected Type(Parcel in) {
                id = in.readInt();
                name = in.readString();
            }

            public static final Creator<Type> CREATOR = new Creator<Type>() {
                @Override
                public Type createFromParcel(Parcel in) {
                    return new Type(in);
                }

                @Override
                public Type[] newArray(int size) {
                    return new Type[size];
                }
            };

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeInt(id);
                dest.writeString(name);
            }
        }

        static class Address implements Parcelable {
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
            Address(Double latitude, Double longitude, String country, String province,
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

            protected Address(Parcel in) {
                latitude = in.readDouble();
                longitude = in.readDouble();
                country = in.readString();
                province = in.readString();
                city = in.readString();
                district = in.readString();
                street = in.readString();
                house = in.readString();
                flat = in.readString();
                postcode = in.readInt();
            }

            public static final Creator<Address> CREATOR = new Creator<Address>() {
                @Override
                public Address createFromParcel(Parcel in) {
                    return new Address(in);
                }

                @Override
                public Address[] newArray(int size) {
                    return new Address[size];
                }
            };

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeDouble(latitude);
                dest.writeDouble(longitude);
                dest.writeString(country);
                dest.writeString(province);
                dest.writeString(city);
                dest.writeString(district);
                dest.writeString(street);
                dest.writeString(house);
                dest.writeString(flat);
                dest.writeInt(postcode);
            }
        }

        static class Photos {
            int id;
            String link;

            Photos(int id, String link) {
                this.id = id;
                this.link = link;
            }
        }

        static class Reviews implements Serializable {

            static class Author implements Serializable {
                int id;
                String name;
                String surname;

                /**
                 * @param id      айди автора
                 * @param name    имя автора
                 * @param surname фамилия автора
                 */
                private Author(int id, String name, String surname) {
                    this.id = id;
                    this.name = name;
                    this.surname = surname;
                }
            }

            int id;
            String body;
            Author author;
            int mark;
            String publishedAt;

            /**
             * @param id          айди обзора
             * @param body        текст обзора
             * @param author      автор обзора
             * @param mark        оценка места
             * @param publishedAt дата публикации обзора
             */
            Reviews(int id, String body, Author author, int mark, String publishedAt) {
                this.id = id;
                this.body = body;
                this.author = author;
                this.mark = mark;
                this.publishedAt = publishedAt;
            }
        }

        int id; //id места
        String name; // имя места
        double mark; // общая оценка места
        Type type;
        Address address;
        String description;
        ArrayList<Photos> photos = new ArrayList<>();
        int reviewsCount;
        ArrayList<Reviews> reviews = new ArrayList<>();
        boolean isVisible = false; //new

        Place() {
        }
    }


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
        /*materialSearchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {

            }

            @Override
            public void onSearchConfirmed(CharSequence text) {
                startSearch(text.toString(), true, null, true);
            }

            @Override
            public void onButtonClicked(int buttonCode) {
                if (buttonCode == MaterialSearchBar.BUTTON_NAVIGATION) {
                    //opening or closing a navigation drawer
                    Intent i = new Intent(MainActivity.this, Search.class); // АНДРЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЙ 2й параметр

                    //i.putExtra("places element", places[Integer.parseInt(PlaceID)]); // контекст - вся инфа о месте - изу структуру!

                    startActivity(i);
                } else if (buttonCode == MaterialSearchBar.BUTTON_BACK) {
                    materialSearchBar.closeSearch();
                }
                Intent i = new Intent(MainActivity.this, Search.class); // АНДРЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЙ 2й параметр

                //i.putExtra("places element", places[Integer.parseInt(PlaceID)]); // контекст - вся инфа о месте - изу структуру!

                startActivity(i);
            }
        });*/

       /* materialSearchBar.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                FindAutocompletePredictionsRequest predictionsRequest = FindAutocompletePredictionsRequest.
                        builder().
                        setTypeFilter(TypeFilter.ADDRESS).
                        setSessionToken(token).
                        setQuery(s.toString()).
                        build();
                placesClient.findAutocompletePredictions(predictionsRequest).addOnCompleteListener(new OnCompleteListener<FindAutocompletePredictionsResponse>() {
                    @Override
                    public void onComplete(@NonNull Task<FindAutocompletePredictionsResponse> task) {
                        if (task.isSuccessful()) {
                            FindAutocompletePredictionsResponse predictionsResponse = task.getResult();
                            if (predictionsResponse != null) {
                                predictionList = predictionsResponse.getAutocompletePredictions();
                                List<String> suggestionsList = new ArrayList<>();
                                for (int i=0; i< predictionList.size();i++){
                                    AutocompletePrediction prediction = predictionList.get(i);
                                    suggestionsList.add(prediction.getFullText(null).toString());
                                }
                                materialSearchBar.updateLastSuggestions(suggestionsList);
                                if (!materialSearchBar.isSuggestionsVisible()){
                                    materialSearchBar.showSuggestionsList();
                                }
                            }
                        } else {
                            Log.i("mytag","prediction failed");
                        }
                    }
                });
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });*/
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

        /*
         * Я тут при клике на маркер перехожу на активити с его описанием.
         */
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                String PlaceID = marker.getSnippet();

                Intent i = new Intent(MainActivity.this, PlaceInside.class); // АНДРЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЙ 2й параметр

                i.putExtra("places element", places[Integer.parseInt(PlaceID)]);
                i.putExtra("places element", userLocationMarker.getPosition());// контекст - вся инфа о месте - изу структуру!
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

    private void parseInterestingPlaces() {
        int count = 0; //поле в JSON, сколько мест в базе всего
        JSONObject data;
        JSONArray elements;
        try {
            data = new JSONObject(Objects.requireNonNull(JsonDataFromAsset("input.json"))).getJSONObject("data");
            count = data.getInt("count");
            elements = data.getJSONArray("elements");
            places = new Place[count];

            for (int i = 0; i < count; i++) {
                places[i] = new Place();
                places[i].id = elements.getJSONObject(i).getInt("id");
                places[i].mark = elements.getJSONObject(i).getDouble("mark");
                places[i].type = new Place.Type(elements.getJSONObject(i).getJSONObject("type").getInt("id"),
                        elements.getJSONObject(i).getJSONObject("type").getString("name"));
                places[i].name = elements.getJSONObject(i).getString("name");
                places[i].address = new Place.Address(elements.getJSONObject(i).getJSONObject("address").getDouble("latitude"),
                        elements.getJSONObject(i).getJSONObject("address").getDouble("longitude"),
                        elements.getJSONObject(i).getJSONObject("address").getString("country"),
                        elements.getJSONObject(i).getJSONObject("address").getString("province"),
                        elements.getJSONObject(i).getJSONObject("address").getString("city"),
                        elements.getJSONObject(i).getJSONObject("address").getString("district"),
                        elements.getJSONObject(i).getJSONObject("address").getString("street"),
                        elements.getJSONObject(i).getJSONObject("address").getString("house"),
                        elements.getJSONObject(i).getJSONObject("address").getString("flat"),
                        elements.getJSONObject(i).getJSONObject("address").getInt("postcode"));
                places[i].description = elements.getJSONObject(i).getString("description");
                places[i].reviewsCount = elements.getJSONObject(i).getJSONObject("reviews").getInt("count");
                for (int j = 0; j < places[i].reviewsCount; j++) {
                    places[i].reviews.add(new Place.Reviews(elements.getJSONObject(i).getJSONObject("reviews").getJSONArray("elements").getJSONObject(j).getInt("id"),
                            elements.getJSONObject(i).getJSONObject("reviews").getJSONArray("elements").getJSONObject(j).getString("body"),
                            new Place.Reviews.Author(elements.getJSONObject(i).getJSONObject("reviews").getJSONArray("elements").getJSONObject(j).getJSONObject("author").getInt("id"),
                                    elements.getJSONObject(i).getJSONObject("reviews").getJSONArray("elements").getJSONObject(j).getJSONObject("author").getString("name"),
                                    elements.getJSONObject(i).getJSONObject("reviews").getJSONArray("elements").getJSONObject(j).getJSONObject("author").getString("surname")
                                    //аватар идет пока в опу
                            ),
                            elements.getJSONObject(i).getJSONObject("reviews").getJSONArray("elements").getJSONObject(j).getInt("mark"),
                            elements.getJSONObject(i).getJSONObject("reviews").getJSONArray("elements").getJSONObject(j).getString("published_at")));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //Gson gson = new Gson();
        //Place a = gson.fromJson(places, new TypeToken<List<Place>>(){}.getType());

        // places = new Place[count];

        /*places[0] = new Place(1, new Place.Type(1, "Архитектура"), "Театр оперы и балета",
                new Place.Address(55.030443, 82.925023, "Россия",
                        "Новосибирская область", "Новосибирск", "Центральный район",
                        "Красный проспект", "", "", 433333), "у мэрии");

        places[1] = new Place(2, new Place.Type(1, "Парки"), "Нарымский сквер",
                new Place.Address(55.043548, 82.909349, "Россия",
                        "Новосибирская область", "Новосибирск", "Центральный район",
                        "Челюскинцев", "", "", 630099), "у цирка");*/


        allPlaces = new ArrayList<>(Arrays.asList(places));

        createInterestingPlaces();
    }
    private String JsonDataFromAsset(String fileName) {
        String json = null;
        try {
            InputStream inputStream = getAssets().open(fileName);
            int sizeOfFile = inputStream.available();
            byte[] bufferData = new byte[sizeOfFile];
            inputStream.read(bufferData);
            inputStream.close();
            json = new String(bufferData, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return json;
    }

    private void createInterestingPlaces() { // new placesFound и placesDraw - разные вещи, ибо нам мб нужно будет показать только парки или т.п.
        MarkerOptions markerOptions = new MarkerOptions();
        Marker newMarker;

        for (int i = 0; i < allPlaces.size(); i++) {
            markerOptions.position(new LatLng(allPlaces.get(i).address.latitude, allPlaces.get(i).address.longitude));
            markerOptions.icon(bitmapDescriptorFromVector(getApplicationContext(), R.drawable.ic_baseline_place_36));
            markerOptions.snippet(Integer.toString(allPlaces.get(i).id - 1)); // я в сниппет сую АЙДИ этого места, чтобы инфу о нем можно было передавать в дургие активитис. Внимание -1, так id с 1, массив с 0
            newMarker = mMap.addMarker(markerOptions);
            markers.add(newMarker);
            newMarker.setVisible(false);
        }

        refreshInterestingPlaces(); //new
    }

    private void refreshInterestingPlaces() { //new
        for (int i = 0; i < allPlaces.size(); i++) {
            Place place = allPlaces.get(i);
            place.isVisible = false;
            allPlaces.set(i, place);
        }
        for (int i = 0; i < allPlaces.size(); i++) {
            for (int j = 0; j < typeArray.length; j++) {
                if (allPlaces.get(i).type.name.equals(typeArray[j]) && !selectedType[j]) {
                    markers.get(i).setVisible(false);
                }
                if (allPlaces.get(i).type.name.equals(typeArray[j]) && selectedType[j]) {
                    markers.get(i).setVisible(true);
                    Place place = allPlaces.get(i);
                    place.isVisible = true;
                    allPlaces.set(i, place);
                }
            }
        }
    }

    private void deleteInterestingPlaces(List<Place> placesToDraw) {
        for (int i = 0; i < placesToDraw.size(); i++) {
            for (int j = 0; j < typeArray.length; j++) {
                if (placesToDraw.get(i).type.name.equals(typeArray[j]) && !selectedType[j]) {
                    markers.get(i).setVisible(false);
                }
            }
        }
    }

    private void drawInterestingPlaces(List<Place> placesToDraw) {
        for (int i = 0; i < placesToDraw.size(); i++) {
            for (int j = 0; j < typeArray.length; j++) {
                if (placesToDraw.get(i).type.name.equals(typeArray[j]) && selectedType[j]) {
                    markers.get(i).setVisible(true);
                }
            }
        }
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