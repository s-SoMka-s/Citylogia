package com.solution.citylogia;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.HashMap;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ProfileActivity extends AppCompatActivity {

    private Boolean isPressed = false;

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

        ImageView but_like = findViewById(R.id.icon_like);

        /*but_like.setOnClickListener(v -> {
            if (!this.place.is_favorite()) {
                but_like.setImageResource(R.drawable.heart_color);
                // выставить флажок, в профиле у человека, что ему место понравилось. (В базе)
                setLike(true);
            } else {
                but_like.setImageResource(R.drawable.heart);
                // убрать из базы данных
                setLike(false);
            }
        });*/
    }

    private void fillLikedPlaces(ConstraintLayout LikedWholeContainer) {

    }

    private boolean getLike() {
        return isPressed;
    }

    /*private void setLike(boolean state) {
        if (state){
            HashMap<String, Object> body = new HashMap<>();
            body.put("place_id", this.place.getId());
            body.put("user_id", 4);
            this.favoritesApi.makeFavorite(body).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(res -> {
                this.isPressed = res.getData() ? true : this.isPressed;
            });
        }
        else {
            this.favoritesApi.deleteFavorite(this.place.getId()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(res -> {
                this.isPressed = res.getData() ? false : this.isPressed;
            });
        }
    }*/
}