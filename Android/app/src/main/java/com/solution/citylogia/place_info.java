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
    private Place place;

    public static place_info newInstance(Bundle bundle) {
        place_info fragment = new place_info();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Bundle args = getArguments();
        super.onCreate(savedInstanceState);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        if (args != null) {
            this.place = gson.fromJson(String.valueOf(args.getSerializable("place")), Place.class);
            System.out.println(this.place);
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