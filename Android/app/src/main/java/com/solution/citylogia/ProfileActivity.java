package com.solution.citylogia;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.solution.citylogia.models.Favorite;
import com.solution.citylogia.models.Place;
import com.solution.citylogia.models.ShortPlace;
import com.solution.citylogia.network.RetrofitSingleton;
import com.solution.citylogia.network.api.IFavoritesApi;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ProfileActivity extends AppCompatActivity {

    private Boolean isPressed = false;
    private IFavoritesApi favoritesApi = RetrofitSingleton.INSTANCE.getRetrofit().create(IFavoritesApi.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        this.loadFavorites();

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

    private void fillLikedPlaces(Place place, View view) {
        TextView name = view.findViewById(R.id.title_v);
        name.setText(place.getName());

        TextView address = view.findViewById(R.id.address_v);
        address.setText(place.getAddress());
        ImageView but_like = view.findViewById(R.id.icon_heart);
        but_like.setPressed(place.is_favorite());

        but_like.setImageResource(R.drawable.heart_color);

    }

    private void loadFavorites() {
        this.favoritesApi.getFavorites().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(res -> {
            List<Favorite> favorites = res.getData().getElements();
            LinearLayout LikedLayoutInsert = findViewById(R.id.LikedLayoutInsert);
            favorites.forEach(favorite -> {
                final View likedPlace = getLayoutInflater().inflate(R.layout.liked_place_add, null, false);
                fillLikedPlaces(favorite.getPlace(), likedPlace);
                LikedLayoutInsert.addView(likedPlace);
            });
        });
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