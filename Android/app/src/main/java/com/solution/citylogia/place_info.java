package com.solution.citylogia;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.solution.citylogia.models.Place;
import com.solution.citylogia.models.PlaceAddress;
import com.solution.citylogia.network.RetrofitSingleton;
import com.solution.citylogia.network.api.IPlaceApi;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class place_info extends Fragment {

    private final IPlaceApi placeApi;

    private Place place = null;

    public place_info() {
        Retrofit retrofit = RetrofitSingleton.INSTANCE.getRetrofit();
        this.placeApi = retrofit.create(IPlaceApi.class);
    }

    public place_info newInstance() {
        place_info fragment = new place_info();
        Bundle args = new Bundle();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        args.putSerializable("place", gson.toJson(this.place));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        if (getArguments() != null) {
            savedInstanceState = getArguments();
            this.place = gson.fromJson((JsonElement) savedInstanceState.getSerializable("placeInfo"), Place.class);
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
        newInstance();

        this.placeApi.getPlaceInfo(2).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(place -> {
            this.place = place.getData();
            this.fillData(view);
            System.out.println(this.place);
        });

        Button but_reviews = view.findViewById(R.id.but_reviews);
        but_reviews.setOnClickListener(v -> navController.navigate(R.id.action_place_info_to_place_reviews));

        ImageView but_back = view.findViewById(R.id.icon_back);
        but_back.setOnClickListener(v -> navController.navigate(R.id.action_place_info_to_activity_place_inside));
    }

    private void fillData(View view) {
        TextView title_v2_replace = view.findViewById(R.id.title_v2);
        TextView address_v2_replace = view.findViewById(R.id.address_v2);
        TextView text_v2_replace = view.findViewById(R.id.text_v2);

        String title_v2 = this.place.getName();
        String address_v2 = this.place.getAddress();
        String text_v2 = this.place.getDescription();

        title_v2_replace.setText(title_v2);
        address_v2_replace.setText(address_v2);
        text_v2_replace.setText(text_v2);
    }
}