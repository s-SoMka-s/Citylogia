package com.solution.citylogia;

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

public class ExampleDialog extends AppCompatDialogFragment {

    private EditText editTextReview;
    private RatingBar ratingBar;
    private float rateValue;
    private String textReview;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.pop_up_review, null);

        builder.setView(view)
                .setNegativeButton("Отменить", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("Отправить", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        textReview = editTextReview.getText().toString(); // here is our review
                        rateValue = ratingBar.getRating(); // here is our rating
                    }
                });

        // send our review to the server. Get in class review from the structure and reload

        editTextReview = view.findViewById(R.id.review);
        ratingBar = view.findViewById(R.id.ratingBar);

        return builder.create();
    }

    public interface ExampleDialogListener {
        void applyTexts();
    }
}
