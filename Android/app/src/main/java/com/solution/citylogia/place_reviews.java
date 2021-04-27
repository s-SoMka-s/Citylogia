package com.solution.citylogia;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.solution.citylogia.models.Place;
import com.solution.citylogia.models.Review;
import com.solution.citylogia.network.RetrofitSingleton;
import com.solution.citylogia.network.api.IFavoritesApi;
import com.solution.citylogia.network.api.IPlaceApi;
import com.solution.citylogia.network.api.IReviewsApi;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.solution.citylogia.utils.DateTimeExtensionsKt.FromTimestamp;

public class place_reviews extends Fragment {

    private Place place = null;
    private Boolean isPressed = false;
    private IFavoritesApi favoritesApi = RetrofitSingleton.INSTANCE.getRetrofit().create(IFavoritesApi.class);

    public place_reviews() {

    }

    public static place_reviews newInstance() {
        place_reviews fragment = new place_reviews();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        Bundle args = getArguments();
        if (args != null) {
            this.place = gson.fromJson(String.valueOf(args.getSerializable("place")), Place.class);
        }
    }


    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_place_reviews, container, false);

        ImageView open_review_v3_1 = view.findViewById(R.id.openReview);
        open_review_v3_1.setOnClickListener(v -> openDialog());

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        LinearLayout reviewLayoutInsert = view.findViewById(R.id.LikedLayoutInsert);
        this.place.getReviews().getElements().forEach(review -> {
            final View cricketerView = getLayoutInflater().inflate(R.layout.review_row_add, null, false);
            reviewLayoutInsert.addView(cricketerView);
            fillReviews(cricketerView.findViewById(R.id.testLayout), review);

        });

        this.fillData(view);

        final NavController navController = Navigation.findNavController(view);

        Button but_info = view.findViewById(R.id.but_info);
        but_info.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            bundle.putSerializable("place", gson.toJson(this.place));
            navController.navigate(R.id.action_place_reviews_to_place_info, bundle); });

        ImageView but_back = view.findViewById(R.id.icon_back_v3);
        but_back.setOnClickListener(v -> navController.navigate(R.id.action_place_reviews_to_activity_place_inside));

        ImageView but_like = view.findViewById(R.id.icon_heart);

        but_like.setOnClickListener(v -> {
            boolean isPressed = getLike();
            if (!this.place.is_favorite()) {
                but_like.setImageResource(R.drawable.heart_color);
                // выставить флажок, в профиле у человека, что ему место понравилось. (В базе)
                setLike(true);
            } else {
                but_like.setImageResource(R.drawable.heart);
                // убрать из базы данных
                setLike(false);
            }
        });
    }

    private boolean getLike() {
        return isPressed;
    }

    private void setLike(boolean state) {
        if (state){
            HashMap<String, Object> body = new HashMap<>();
            body.put("place_id", this.place.getId());
            body.put("user_id", 4);
            this.favoritesApi.makeFavorite(body).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(res -> {
                this.isPressed = res.getData() ? true : this.isPressed;
                this.place.set_favorite(this.isPressed);
            });
        }
        else {
            this.favoritesApi.deleteFavorite(this.place.getId()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(res -> {
                this.isPressed = res.getData() ? false : this.isPressed;
                this.place.set_favorite(this.isPressed);
            });
        }


    }

    public void openDialog() {
        ReviewPopUp reviewPopUp = new ReviewPopUp();
        reviewPopUp.show(getChildFragmentManager(), "text");
    }


    private void fillData(View view) {
        TextView title_v3_replace = view.findViewById(R.id.title_v3);
        TextView address_v3_replace = view.findViewById(R.id.address_v3);

        String title_v2 = this.place.getName();
        String address_v2 = this.place.getAddress();
        // Boolean isLikePressed = ...

        title_v3_replace.setText(title_v2);
        address_v3_replace.setText(address_v2);

        ImageView but_like = view.findViewById(R.id.icon_heart);
        but_like.setPressed(this.place.is_favorite());

        if (but_like.isPressed()) {
            but_like.setImageResource(R.drawable.heart_color);
        }

        ImageView placeImage = view.findViewById(R.id.image_replace);
        String url_image = place.getPhoto().getElements().get(1).getPublic_url();
        Picasso.get().load(url_image)
                .placeholder(R.drawable.basic_person)
                .into(placeImage);
    }

    private void fillReviews(ConstraintLayout reviewLayout, Review review) {

        TextView name = reviewLayout.findViewById(R.id.reviewName);
        TextView comment = reviewLayout.findViewById(R.id.reviewComment);
        TextView date = reviewLayout.findViewById(R.id.reviewDate);
        ImageView image = reviewLayout.findViewById(R.id.reviewImage);
        ImageView rateImage = reviewLayout.findViewById(R.id.reviewRate);

        // set up rate
        double rate = review.getMark();
        setRate(rateImage, rate);

        // set up author name
        String nameReplace =  review.getAuthor().getName();
        name.setText(nameReplace);

        // set up text of review
        String commentReplace = review.getBody();
        comment.setText(commentReplace);

        // set up date of publishing
        String dateReplace = FromTimestamp(review.getPublished_at());
        date.setText(dateReplace);
        //String date_review_v3_1 = review.getPublished_at().format(DateTimeFormatter.ISO_LOCAL_DATE);

        String url_image = review.getAuthor().getAvatar().getPublic_url();

        Picasso.get().load(url_image)
                .resize(150, 150)
                .centerCrop()
                .placeholder(R.drawable.basic_person)
                .into(image);
    }

    private void setRate(ImageView rateImage, double rate) {
        Drawable myDrawable;
        switch ((int)rate) {
            case 5:
                myDrawable = getResources().getDrawable(R.drawable.rate_5);
                rateImage.setImageDrawable(myDrawable);
                break;
            case 4:
                myDrawable = getResources().getDrawable(R.drawable.rate_4);
                rateImage.setImageDrawable(myDrawable);
                break;
            case 3:
                myDrawable = getResources().getDrawable(R.drawable.rate_3);
                rateImage.setImageDrawable(myDrawable);
                break;
            case 2:
                myDrawable = getResources().getDrawable(R.drawable.rate_2);
                rateImage.setImageDrawable(myDrawable);
                break;
            case 1:
                myDrawable = getResources().getDrawable(R.drawable.rate_1);
                rateImage.setImageDrawable(myDrawable);
                break;
        }
    }
}