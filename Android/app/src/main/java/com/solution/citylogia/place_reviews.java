package com.solution.citylogia;

import android.icu.text.DateFormat;
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

import com.solution.citylogia.models.BaseCollectionClass;
import com.solution.citylogia.models.Photo;
import com.solution.citylogia.models.Place;
import com.solution.citylogia.models.Review;
import com.solution.citylogia.models.User;
import com.squareup.picasso.Picasso;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link place_reviews#newInstance} factory method to
 * create an instance of this fragment.
 */
public class place_reviews extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public place_reviews() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment place_reviews.
     */
    // TODO: Rename and change types and number of parameters
    public static place_reviews newInstance(String param1, String param2) {
        place_reviews fragment = new place_reviews();
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
        View view = inflater.inflate(R.layout.fragment_place_reviews, container, false);

        ImageView open_review_v3_1 = view.findViewById(R.id.openReview);

        open_review_v3_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });


        TextView name_v3_1_replace = view.findViewById(R.id.name_v3_1);
        TextView comment_v3_1_replace = view.findViewById(R.id.comment_v3_1);
        TextView date_v3_1_replace = view.findViewById(R.id.date_v3_1);
        ImageView image_v3_1_replace = view.findViewById(R.id.image_v3_1);

        Place place = new Place();
        Review review = new Review();
        User user = new User();
        user = review.getAuthor();
        String name_v3_1 = user.getName();
        String comment_v3_1 = review.getBody();


        // так как нет в коллекции фото выкидываем с нулл поинтером
        //Photo image_v3_1 = place.getPhotos().getElements().get(1);
        //String url_image = image_v3_1.getLink();;

        String url_image = "https://sun9-15.userapi.com/impf/c631924/v631924846/245f1/0OxkD0nPXqY.jpg?size=762x1104&quality=96&sign=4ec3533e9b0e4edb1b058368ed04ec62&type=album";

        Picasso.get().load(url_image)
                .resize(150, 150)
                .centerCrop()
                .placeholder(R.drawable.ic_launcher_background)
                .into(image_v3_1_replace);

        String date_review_v3_1 = review.getPublished_at().format(DateTimeFormatter.ISO_LOCAL_DATE);

        name_v3_1_replace.setText(name_v3_1);

        comment_v3_1_replace.setText(comment_v3_1);
        date_v3_1_replace.setText(date_review_v3_1);
        
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final NavController navController = Navigation.findNavController(view);

        Button but_info = view.findViewById(R.id.but_info);
        but_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_place_reviews_to_place_info);
            }
        });
    }

    public void openDialog() {
        ExampleDialog exampleDialog = new ExampleDialog();
        exampleDialog.show(getChildFragmentManager(), "text");
    }
}