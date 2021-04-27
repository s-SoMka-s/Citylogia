package com.solution.citylogia;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.solution.citylogia.models.Place;
import com.solution.citylogia.models.Review;
import com.squareup.picasso.Picasso;

public class place_reviews extends Fragment {

    private Place place = null;
    private Boolean isPressed = false;

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
            bundle.putSerializable("place", gson.toJson(place));
            navController.navigate(R.id.action_place_reviews_to_place_info, bundle); });

        ImageView but_back = view.findViewById(R.id.icon_back_v3);
        but_back.setOnClickListener(v -> navController.navigate(R.id.action_place_reviews_to_activity_place_inside));

        ImageView but_like = view.findViewById(R.id.icon_heart);

        but_like.setOnClickListener(v -> {
            boolean isPressed = getLike();
            if (!isPressed) {
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
        isPressed = state;
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

        /*if (isLikePressed) {
            ImageView but_like = view.findViewById(R.id.icon_heart);
            but_like.setImageResource(R.drawable.heart_color);
        }*/

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
        String dateReplace = review.getPublished_at();
        date.setText(dateReplace);
        //String date_review_v3_1 = review.getPublished_at().format(DateTimeFormatter.ISO_LOCAL_DATE);


        // так как нет в коллекции фото выкидываем с нулл поинтером
        //Photo image_v3_1 = place.getPhotos().getElements().get(1);
        //String url_image = image_v3_1.getLink();;

        String url_image = "https://sun9-15.userapi.com/impf/c631924/v631924846/245f1/0OxkD0nPXqY.jpg?size=762x1104&quality=96&sign=4ec3533e9b0e4edb1b058368ed04ec62&type=album";

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