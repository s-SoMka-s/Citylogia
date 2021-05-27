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
import com.solution.citylogia.network.RetrofitSingleton;
import com.solution.citylogia.network.api.IFavoritesApi;
import com.solution.citylogia.network.api.IProfileApi;
import com.solution.citylogia.services.AuthorizationService;
import com.squareup.picasso.Picasso;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.EntryPoint;
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
    private ImageButton logoutBtn;

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

        ImageView but_like = findViewById(R.id.icon_like);
        ImageButton addProfileImg = findViewById(R.id.add_img_btn);
        profileImage = findViewById(R.id.profile_img);
        logoutBtn = findViewById(R.id.btn_logout);

        addProfileImg.setOnClickListener(v -> {
            imageFromGallery();
        });

        logoutBtn.setOnClickListener(v -> authorizationService.logOut());

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

    @SuppressLint("CheckResult")
    private void loadProfile() {
        this.profileApi.get().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(res -> {
            TextView name = this.findViewById(R.id.profile_name);
            name.setText(res.getData().getName());
        });
    }

    private void fillLikedPlaces(Place place, View view) {
        TextView name = view.findViewById(R.id.title_v);
        name.setText(place.getName());

        TextView address = view.findViewById(R.id.address_v);
        address.setText(place.getAddress());
        ImageView but_like = view.findViewById(R.id.icon_like);
        but_like.setPressed(true);

        but_like.setImageResource(R.drawable.heart_color);

        ImageView placeImage = view.findViewById(R.id.placeLikedImage);
        placeImage.setOnClickListener( v -> {
            Long placeId = place.getId();
            Intent i = new Intent(this, PlaceInside.class);
            i.putExtra("id", placeId);
            startActivity(i);
            finish();
        });
        try {
            String url_image = place.getPhoto().getElements().get(0).getPublic_url();
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
            LinearLayout likedLayoutInsert = this.findViewById(R.id.LikedLayoutInsert);
            favorites.forEach(favorite -> {
                View cricketerView = getLayoutInflater().inflate(R.layout.liked_place_add, null, false);
                fillLikedPlaces(favorite.getPlace(), cricketerView.findViewById(R.id.LikedWholeContainer));
                likedLayoutInsert.addView(cricketerView);
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

    private void imageFromGallery() {
        Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
        getIntent.setType("image/*");

        Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");

        Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {pickIntent});

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