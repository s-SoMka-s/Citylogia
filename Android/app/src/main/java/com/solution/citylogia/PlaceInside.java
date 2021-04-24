package com.solution.citylogia;


import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;



public class PlaceInside extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        Bundle a = getIntent().getBundleExtra("id");
        System.out.println(a);
        //getSupportActionBar().hide();
    }

}