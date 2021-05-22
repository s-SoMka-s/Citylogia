package com.solution.citylogia;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.widget.Button;

import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mancj.materialsearchbar.MaterialSearchBar;

import java.util.List;
import java.util.Locale;


import dagger.hilt.android.AndroidEntryPoint;

public class MainActivity extends Activity {
    Button filter;

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


        filter = findViewById(R.id.bt_filter);
        materialSearchBar = findViewById(R.id.searchBar);
        materialSearchBar.setOnClickListener(v -> {
            Intent i = new Intent(MainActivity.this, Search.class);
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            //i.putExtra("places", gson.toJson(this.places));
            //i.putExtra("selected types", selectedTypes);
            //LatLng position = userLocationMarker.getPosition();
            //i.putExtra("user position", position);
            startActivityForResult(i, 2404);

        });
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode == 2404) {
            if(data != null) {
                int index = data.getIntExtra("selected places in search",-1);
                //LatLng latLng = markers.get(index).getPosition();
                //mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17));
            }
        }
    }

}