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

import com.solution.citylogia.models.BaseCollectionResponse;
import com.solution.citylogia.models.BaseObjectResponse;
import com.solution.citylogia.models.Place;

import com.solution.citylogia.models.ShortPlace;
import com.solution.citylogia.network.RetrofitSingleton;
import com.solution.citylogia.network.api.IPlaceApi;

import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link activity_place_inside#newInstance} factory method to
 * create an instance of this fragment.
 */
public class activity_place_inside extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private final IPlaceApi placeApi;
    private ShortPlace placeInfo = null;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public activity_place_inside() {
        // Required empty public constructor
        Retrofit retrofit = RetrofitSingleton.INSTANCE.getRetrofit();
        this.placeApi = retrofit.create(IPlaceApi.class);
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment activity_place_inside.
     */
    // TODO: Rename and change types and number of parameters
    public static activity_place_inside newInstance(String param1, String param2) {
        activity_place_inside fragment = new activity_place_inside();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_place_inside, container, false);

        this.placeApi.getAllPlaces().subscribeOn(Schedulers.io()).subscribe(places -> {
            this.placeInfo = places.getData().getElements().get(0);
            this.setData(view, this.placeInfo);
        });

        return view;
    }

    public void setData (View view, ShortPlace place) {
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
    }
}