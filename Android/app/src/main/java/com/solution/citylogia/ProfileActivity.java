package com.solution.citylogia;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        LinearLayout LikedLayoutInsert = findViewById(R.id.LikedLayoutInsert);
        for (int i = 0; i < 5; i++) {
            final View likedPlace = getLayoutInflater().inflate(R.layout.liked_place_add, null, false);
            LikedLayoutInsert.addView(likedPlace);
            fillLikedPlaces(likedPlace.findViewById(R.id.LikedWholeContainer));
        }

        ImageButton map_but = findViewById(R.id.map_icon);
        map_but.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), MapActivity.class));
            finish();
        });
    }

    private void fillLikedPlaces(ConstraintLayout LikedWholeContainer) {

    }
}