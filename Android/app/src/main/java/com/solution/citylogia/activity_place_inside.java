package com.solution.citylogia;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.solution.citylogia.models.BaseCollectionResponse;
import com.solution.citylogia.models.BaseObjectResponse;
import com.solution.citylogia.models.Place;

import com.solution.citylogia.models.ShortPlace;
import com.solution.citylogia.network.RetrofitSingleton;
import com.solution.citylogia.network.api.IPlaceApi;

import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;
import kotlinx.serialization.json.Json;
import retrofit2.Retrofit;


public class activity_place_inside extends Fragment {

    private final IPlaceApi placeApi;
    private Place placeInfo = null;


    public activity_place_inside() {
        // Required empty public constructor
        Retrofit retrofit = RetrofitSingleton.INSTANCE.getRetrofit();
        this.placeApi = retrofit.create(IPlaceApi.class);
    }

    public activity_place_inside newInstance() {
        activity_place_inside fragment = new activity_place_inside();
        Bundle args = new Bundle();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        args.putSerializable("placeInfo", gson.toJson(this.placeInfo));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_place_inside, container, false);

        this.placeApi.getPlaceInfo(2).subscribeOn(Schedulers.io()).subscribe(place -> {
            this.placeInfo = place.getData();
            this.setData(view, this.placeInfo);
        });

        return view;
    }

    public void setData (View view, Place place) {
        TextView title_v1_replace = view.findViewById(R.id.title_v1);
        TextView text_v1_replace = view.findViewById(R.id.text_v1);

        String title_v1 = place.getName();
        String text_v1 = "Тут будет описание";

        title_v1_replace.setText(title_v1);
        //text_v1_replace.setText(text_v1);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final NavController navController = Navigation.findNavController(view);

        Button but_more = view.findViewById(R.id.but_more);
        but_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_activity_place_inside_to_place_info);
            }
        });

        Button but_set_route = view.findViewById(R.id.set_route);
        but_set_route.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        newInstance();
    }
}