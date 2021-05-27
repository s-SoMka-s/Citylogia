package com.solution.citylogia;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.viewpager2.widget.ViewPager2;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.solution.citylogia.models.Place;
import com.solution.citylogia.network.RetrofitSingleton;
import com.solution.citylogia.network.api.IPlaceApi;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class activity_place_inside extends Fragment {
    private PlaceInsideAdapter placeInsideAdapter;
    private final IPlaceApi placeApi;
    private Place placeInfo = null;
    private Long id;

    private RetrofitSingleton retrofit;


    public activity_place_inside() {
        // Required empty public constructor
        this.placeApi = retrofit.getRetrofit().create(IPlaceApi.class);
    }

    public activity_place_inside newInstance() {
        activity_place_inside fragment = new activity_place_inside();
        Bundle args = new Bundle();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        args.putSerializable("place", gson.toJson(this.placeInfo));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUpBoardingItems();

        this.id = (Long)requireActivity().getIntent().getExtras().get("id");
        System.out.println("идентификатор места" + id);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_place_inside, container, false);


        return view;
    }

    public void setData (View view, Place place) {
        TextView name = view.findViewById(R.id.title_v1);
        TextView shortDescription = view.findViewById(R.id.text_v1);

        String title_v1 = place.getName();
        shortDescription.setText(place.getShort_description());

        name.setText(title_v1);
        /*
        ImageView placeImage = view.findViewById(R.id.image_inside);

        try {
            String url_image = placeInfo.getPhoto().getElements().get(0).getPublic_url();
            Picasso.get().load(url_image)
                    .placeholder(R.drawable.tm_info)
                    .into(placeImage);
        } catch (Exception e) {
            e.printStackTrace();
        }

         */
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.placeApi.getPlaceInfo(this.id).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(place -> {
            this.placeInfo = place.getData();
            this.setData(view, this.placeInfo);
        });
        ViewPager2 placeInsidePager = view.findViewById(R.id.place_inside_viewpager);
        placeInsidePager.setAdapter(placeInsideAdapter);
        placeInsidePager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
            }
        });
        final NavController navController = Navigation.findNavController(view);

        Button but_more = view.findViewById(R.id.but_more);
        but_more.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            bundle.putSerializable("place", gson.toJson(placeInfo));

            navController.navigate(R.id.action_activity_place_inside_to_place_info, bundle);
        });

        Button but_set_route = view.findViewById(R.id.set_route);
        but_set_route.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
    }

    private void setUpBoardingItems() {
        List<PlaceInsideItem> mList = new ArrayList<>();

        mList.add(new PlaceInsideItem(R.drawable.nsu_1));
        mList.add(new PlaceInsideItem( R.drawable.park_2));
        mList.add(new PlaceInsideItem(R.drawable.theater_3));

        placeInsideAdapter = new PlaceInsideAdapter(mList);
    }
}