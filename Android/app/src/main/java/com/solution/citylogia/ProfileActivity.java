package com.solution.citylogia;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.solution.citylogia.models.Favorite;
import com.solution.citylogia.models.Place;
import com.solution.citylogia.models.ShortPlace;
import com.solution.citylogia.network.RetrofitSingleton;
import com.solution.citylogia.network.api.IFavoritesApi;
import com.solution.citylogia.network.api.IProfileApi;
import com.solution.citylogia.services.AuthorizationService;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@AndroidEntryPoint
public class ProfileActivity extends AppCompatActivity {

    private final int PICK_IMAGE = 100;

    private Boolean isPressed = false;
    private IFavoritesApi favoritesApi;
    private IProfileApi profileApi;
    private ImageView profileImage;
    private TextView tipFav;

    @Inject
    RetrofitSingleton retrofit;

    @Inject
    AuthorizationService authorizationService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        this.favoritesApi = retrofit.getRetrofit().create(IFavoritesApi.class);
        this.profileApi = retrofit.getRetrofit().create(IProfileApi.class);
        this.loadProfile();
        this.loadFavorites();

        ImageButton map_but = findViewById(R.id.map_icon);
        map_but.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), MapActivity.class));
            finish();
        });

        ImageButton addProfileImg = findViewById(R.id.add_img_btn);
        profileImage = findViewById(R.id.profile_img);
        ImageButton logoutBtn = findViewById(R.id.btn_logout);
        tipFav = findViewById(R.id.tipFav);

        addProfileImg.setOnClickListener(v -> {
            imageFromGallery();
        });

        logoutBtn.setOnClickListener(v -> {
            authorizationService.logOut();
            Intent i = new Intent(this, LoginActivity.class);
            startActivity(i);
            finish();
        });


    }

    @SuppressLint("CheckResult")
    private void loadProfile() {
        this.profileApi.get().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(res -> {
            TextView name = this.findViewById(R.id.profile_name);
            name.setText(res.getData().getName());
        });
    }

    private void fillLikedPlaces(Favorite favorite, View view) {
        ShortPlace place = favorite.getPlace();

        TextView name = view.findViewById(R.id.title_v);
        name.setText(place.getName());

        TextView address = view.findViewById(R.id.address_v);
        address.setText(place.getCity());
        ImageView but_like = view.findViewById(R.id.icon_like);
        but_like.setPressed(true);

        but_like.setImageResource(R.drawable.heart_color);

        but_like.setOnClickListener(v -> {
            this.removeFavorite(favorite.getId());
        });

        ImageView placeImage = view.findViewById(R.id.placeLikedImage);
        placeImage.setOnClickListener(v -> {
            Long placeId = place.getId();
            Intent i = new Intent(this, PlaceInside.class);
            i.putExtra("id", placeId);
            startActivity(i);
            finish();
        });
        try {
            String url_image = place.getMain_photo().getLink();
            Picasso.get().load(url_image)
                    .placeholder(R.drawable.image_template)
                    .into(placeImage);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @SuppressLint("CheckResult")
    private void loadFavorites() {
        this.favoritesApi.getFavorites().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(res -> {
            List<Favorite> favorites = res.getData().getElements();
            if (favorites.size() > 0) {
                tipFav.setVisibility(View.INVISIBLE);
            } else {
                tipFav.setVisibility(View.VISIBLE);
            }
            LinearLayout likedLayoutInsert = this.findViewById(R.id.LikedLayoutInsert);
            likedLayoutInsert.removeAllViews();

            favorites.forEach(favorite -> {
                View cricketerView = getLayoutInflater().inflate(R.layout.liked_place_add, null, false);
                fillLikedPlaces(favorite, cricketerView.findViewById(R.id.LikedWholeContainer));
                likedLayoutInsert.addView(cricketerView);
            });
        });
    }

    @SuppressLint("CheckResult")
    private void removeFavorite(long id) {
        this.favoritesApi.deleteFavorite(id).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(res -> {
            this.loadFavorites();
        });
    }

    private void imageFromGallery() {
        Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
        getIntent.setType("image/*");

        Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");

        Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent});

        startActivityForResult(chooserIntent, PICK_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK) {
            profileImage.setImageURI(data.getData());
        }
    }
}