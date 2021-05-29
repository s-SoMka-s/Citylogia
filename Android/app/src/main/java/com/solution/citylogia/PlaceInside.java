package com.solution.citylogia;


import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import dagger.hilt.android.AndroidEntryPoint;


@AndroidEntryPoint
public class PlaceInside extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_inside);
    }

}