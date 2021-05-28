package com.solution.citylogia;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.Fragment;

import com.solution.citylogia.network.RetrofitSingleton;
import com.solution.citylogia.network.api.IReviewsApi;

import java.util.HashMap;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@AndroidEntryPoint
public class ReviewPopUp extends AppCompatDialogFragment {


    private EditText editTextReview;
    private RatingBar ratingBar;
    private float rateValue;
    private String textReview;
    private Long placeId;
    @Inject
    RetrofitSingleton retrofit;
    private IReviewsApi reviewsApi;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        this.reviewsApi = retrofit.getRetrofit().create(IReviewsApi.class);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        this.placeId = (Long)requireActivity().getIntent().getExtras().get("id");
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.pop_up_review, null);

        builder.setView(view)
                .setNegativeButton("Отменить", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("Отправить", new DialogInterface.OnClickListener() {
                    @SuppressLint("CheckResult")
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        textReview = editTextReview.getText().toString(); // here is our review
                        rateValue = ratingBar.getRating(); // here is our rating
                        HashMap<String, Object> body = new HashMap<>();
                        body.put("mark", rateValue);
                        body.put("body", textReview);

                        reviewsApi.addReview(placeId, body).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(res ->{
                                System.out.println("Add review result " + res);
                        });
                    }
                });

        editTextReview = view.findViewById(R.id.review);
        ratingBar = view.findViewById(R.id.ratingBar);

        return builder.create();
    }

    public interface ExampleDialogListener {
        void applyTexts();
    }
}
