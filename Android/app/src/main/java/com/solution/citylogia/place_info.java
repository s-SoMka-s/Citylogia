package com.solution.citylogia;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.viewpager2.widget.ViewPager2;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.solution.citylogia.models.Place;

import java.util.ArrayList;
import java.util.List;

public class place_info extends Fragment {
    private Place place;
    private Long id;

    private InfoCardAdapter infoCardAdapter;
    private LinearLayout layoutCardIndicators;

    public static place_info newInstance(Bundle bundle) {
        place_info fragment = new place_info();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        if (args != null) {
            this.place = gson.fromJson(String.valueOf(args.getSerializable("place")), Place.class);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_place_info, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final NavController navController = Navigation.findNavController(view);
        this.fillData(view);
        Button but_reviews = view.findViewById(R.id.but_reviews);
        but_reviews.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            bundle.putSerializable("place", gson.toJson(place));
            navController.navigate(R.id.action_place_info_to_place_reviews,bundle);
        });

        ImageView but_back = view.findViewById(R.id.icon_back_v3);
        but_back.setOnClickListener(v -> navController.navigate(R.id.action_place_info_to_activity_place_inside));

        ImageButton profile_but = view.findViewById(R.id.profile_but);
        profile_but.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ProfileActivity.class);
            startActivity(intent);
        });

        ImageButton map_btn = view.findViewById(R.id.map_icon);
        map_btn.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), MapActivity.class);
            startActivity(intent);
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

    private void fillData(View view) {
        TextView title_v2_replace = view.findViewById(R.id.title_v3);
        TextView address_v2_replace = view.findViewById(R.id.address_v3);
        TextView text_v2_replace = view.findViewById(R.id.text_intro);

        String title_v2 = this.place.getName();
        String address_v2 = this.place.getCity();
        String text_v2 = this.place.getDescription();

        title_v2_replace.setText(title_v2);
        address_v2_replace.setText(address_v2);
        text_v2_replace.setText(text_v2);
    }
}