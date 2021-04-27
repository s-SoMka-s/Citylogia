package com.solution.citylogia;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.view.View;
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
    }

    private void fillLikedPlaces(ConstraintLayout LikedWholeContainer) {

    }
}