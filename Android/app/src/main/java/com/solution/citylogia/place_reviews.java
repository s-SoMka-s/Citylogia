package com.solution.citylogia;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.media.Image;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.solution.citylogia.models.Place;
import com.solution.citylogia.models.Review;
import com.solution.citylogia.models.User;
import com.squareup.picasso.Picasso;

public class place_reviews extends Fragment {

    private Place place = null;

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

        LinearLayout reviewLayoutInsert = view.findViewById(R.id.reviewLayoutInsert);
        this.place.getReviews().getElements().forEach(review -> {
            final View cricketerView = getLayoutInflater().inflate(R.layout.review_row_add, null, false);
            reviewLayoutInsert.addView(cricketerView);
            fillData(cricketerView.findViewById(R.id.testLayout), review);

        });

        final NavController navController = Navigation.findNavController(view);

        Button but_info = view.findViewById(R.id.but_info);
        but_info.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            bundle.putSerializable("place", gson.toJson(place));
            navController.navigate(R.id.action_place_reviews_to_place_info, bundle); });
    }
  
    public void openDialog() {
        ExampleDialog exampleDialog = new ExampleDialog();
        exampleDialog.show(getChildFragmentManager(), "text");
    }


    private void fillData(ConstraintLayout reviewLayout, Review review) {

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