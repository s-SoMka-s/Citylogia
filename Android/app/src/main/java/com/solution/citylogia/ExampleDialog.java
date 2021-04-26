package com.solution.citylogia;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.dynamic.SupportFragmentWrapper;
import com.solution.citylogia.models.Review;
import com.solution.citylogia.network.RetrofitSingleton;
import com.solution.citylogia.network.api.IReviewsApi;

import java.util.HashMap;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import kotlin.Pair;

import static androidx.core.os.BundleKt.bundleOf;
import static androidx.fragment.app.FragmentKt.setFragmentResult;

public class ExampleDialog extends DialogFragment {

    private EditText editTextReview;
    private RatingBar ratingBar;
    private float rateValue;
    private String textReview;
    private Long placeId;
    private IReviewsApi reviewsApi = RetrofitSingleton.INSTANCE.getRetrofit().create(IReviewsApi.class);

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
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
                        body.put("user_id", 4);

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
