package com.solution.citylogia;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.solution.citylogia.models.Place;
import com.solution.citylogia.models.PlaceAddress;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link place_info#newInstance} factory method to
 * create an instance of this fragment.
 */
public class place_info extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public place_info() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment place_info.
     */
    // TODO: Rename and change types and number of parameters
    public static place_info newInstance(String param1, String param2) {
        place_info fragment = new place_info();
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
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_place_info, container, false);

        ImageView but_back = view.findViewById(R.id.icon_back);

        TextView title_v2_replace = view.findViewById(R.id.title_v2);
        TextView address_v2_replace = view.findViewById(R.id.address_v2);
        TextView text_v2_replace = view.findViewById(R.id.text_v2);

        Place place = new Place();
        String title_v2 = place.getName();
        String address_v2 = place.getAddress();
        String text_v2 = place.getDescription();

        title_v2_replace.setText(title_v2);
        address_v2_replace.setText(address_v2);
        text_v2_replace.setText(text_v2);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final NavController navController = Navigation.findNavController(view);

        Button but_reviews = view.findViewById(R.id.but_reviews);
        but_reviews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_place_info_to_place_reviews);
            }
        });


        ImageView but_back = view.findViewById(R.id.icon_back);
        but_reviews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //navController.navigate(R.id.action_place_info_to_place_reviews);
            }
        });
    }
}