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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.solution.citylogia.models.Place;
import com.solution.citylogia.models.Review;
import com.solution.citylogia.network.RetrofitSingleton;
import com.solution.citylogia.network.api.IFavoritesApi;
import com.solution.citylogia.network.api.IReviewsApi;
import com.solution.citylogia.services.AuthorizationService;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.solution.citylogia.utils.DateTimeExtensionsKt.FromTimestamp;

@AndroidEntryPoint
public class place_reviews extends Fragment {

    private Place place = null;
    private Boolean isPressed = false;

    private IFavoritesApi favoritesApi;
    private IReviewsApi reviewsApi;
    private InfoCardAdapter infoCardAdapter;
    private LinearLayout layoutCardIndicators;

    @Inject
    AuthorizationService authService;

    @Inject
    RetrofitSingleton retrofit;

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

        this.favoritesApi = retrofit.getRetrofit().create(IFavoritesApi.class);
        this.reviewsApi = retrofit.getRetrofit().create(IReviewsApi.class);

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

        FloatingActionButton open_review_v3_1 = view.findViewById(R.id.openReview);

        open_review_v3_1.setOnClickListener(v -> {
            if (authService.isLoggedIn()) {
                openDialog();
            } else {
                Toast.makeText(requireActivity(), "Ошибка, создайте/войдите\n в аккаунт!", Toast.LENGTH_LONG).show();
            }
        });

        return view;
    }

    @SuppressLint("CheckResult")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        LinearLayout reviewLayoutInsert = view.findViewById(R.id.LikedLayoutInsert);

        this.reviewsApi.get(this.place.getId()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(res -> {
            res.getData().getElements().forEach(review -> {
                final View cricketerView = getLayoutInflater().inflate(R.layout.review_row_add, null, false);
                reviewLayoutInsert.addView(cricketerView);
                fillReviews(cricketerView.findViewById(R.id.testLayout), review);
            });
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

            if (authService.isLoggedIn()) {
                if (!this.place.is_favorite()) {
                    but_like.setImageResource(R.drawable.heart_color);
                    setLike(true);
                } else {
                    but_like.setImageResource(R.drawable.heart);
                    setLike(false);
                }
            } else {
                Toast.makeText(requireActivity(), "Ошибка, создайте/войдите\n в аккаунт!", Toast.LENGTH_LONG).show();
            }
        });


        layoutCardIndicators = view.findViewById(R.id.layoutBoardingIndicatorsRev);

        setUpBoardingItems();

        ViewPager2 infoCardPager = view.findViewById(R.id.screen_viewpager_rev);
        infoCardPager.setAdapter(infoCardAdapter);

        setCardIndicators();
        setCurrentIndicator(0);

        infoCardPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                setCurrentIndicator(position);
            }
        });
    }

    private void setUpBoardingItems() {
        List<InfoCardItem> mList = new ArrayList<>();

        infoCardAdapter = new InfoCardAdapter(mList);

        for (int i = 0; i < place.getPhotos().getElements().size(); i++) {
            String url_image = place.getPhotos().getElements().get(i).getLink();
            mList.add(new InfoCardItem(url_image));
        }
    }

    private void setCardIndicators() {
        ImageView[] indicators = new ImageView[infoCardAdapter.getItemCount()];
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMargins(32,0,0,0);

        for (int i = 0; i < indicators.length; i++) {
            indicators[i] = new ImageView(getActivity().getApplicationContext());
            indicators[i].setImageDrawable(ContextCompat.getDrawable(
                    getActivity().getApplicationContext(), R.drawable.indicator_info_inactive
            ));
            indicators[i].setLayoutParams(layoutParams);
            layoutCardIndicators.addView(indicators[i]);
        }
    }

    private void setCurrentIndicator(int index) {
        int childCount = layoutCardIndicators.getChildCount();
        for (int i = 0; i < childCount; i++) {
            ImageView imageView = (ImageView) layoutCardIndicators.getChildAt(i);
            if (i == index) {
                imageView.setImageDrawable(ContextCompat.getDrawable(getActivity().getApplicationContext(), R.drawable.indicator_info_active));
            } else {
                imageView.setImageDrawable(ContextCompat.getDrawable(getActivity().getApplicationContext(), R.drawable.indicator_info_inactive));
            }
        }
    }

    @SuppressLint("CheckResult")
    private void setLike(boolean state) {
        HashMap<String, Object> body = new HashMap<>();
        body.put("place_id", this.place.getId());
        if (state){
            this.favoritesApi.makeFavorite(body).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(res -> {
                this.isPressed = res.getData() ? true : this.isPressed;
                this.place.set_favorite(this.isPressed);
            });
        }
        else {
            this.favoritesApi.getFavorites().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(response -> {
                Long id = response.getData().getElements().stream().filter(f -> f.getPlace().getId() == this.place.getId()).findFirst().get().getId();
                this.favoritesApi.deleteFavorite(id).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(res -> {
                    this.isPressed = res.getData() ? false : this.isPressed;
                    this.place.set_favorite(this.isPressed);
                });
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
        String address_v2 = this.place.getCity();
        // Boolean isLikePressed = ...

        title_v3_replace.setText(title_v2);
        address_v3_replace.setText(address_v2);

        ImageView but_like = view.findViewById(R.id.icon_heart);
        but_like.setPressed(this.place.is_favorite());

        if (but_like.isPressed()) {
            but_like.setImageResource(R.drawable.heart_color);
        }

        ImageView placeImage = view.findViewById(R.id.image_replace);

        try {
            String url_image = place.getPhotos().getElements().get(1).getLink();
            Picasso.get().load(url_image)
                    .placeholder(R.drawable.tm_info)
                    .into(placeImage);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        
        try {
            String url_image = review.getAuthor().getAvatar().getLink();
            Picasso.get().load(url_image)
                    .resize(150, 150)
                    .centerCrop()
                    .placeholder(R.drawable.basic_person)
                    .into(image);
        } catch (Exception e) {
            e.printStackTrace();
        }
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