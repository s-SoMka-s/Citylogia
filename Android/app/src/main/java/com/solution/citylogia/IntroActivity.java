
package com.solution.citylogia;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.solution.citylogia.services.AuthorizationService;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class IntroActivity extends AppCompatActivity {

    private IntroBoardingAdapter introBoardingAdapter;
    private LinearLayout layoutBoardingIndicators;
    private Button buttonAction;
    @Inject
    AuthorizationService authorizationService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.authorizationService.logOut();
        setContentView(R.layout.activity_intro);

        layoutBoardingIndicators = findViewById(R.id.layoutBoardingIndicatorsRev);
        buttonAction = findViewById(R.id.button);

        setUpBoardingItems();

        ViewPager2 introBoardingPager = findViewById(R.id.screen_viewpager_rev);
        introBoardingPager.setAdapter(introBoardingAdapter);

        setBoardingIndicators();
        setCurrentIndicator(0);

        introBoardingPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                setCurrentIndicator(position);
            }
        });

        buttonAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (introBoardingPager.getCurrentItem() + 1 < introBoardingAdapter.getItemCount()) {
                    introBoardingPager.setCurrentItem(introBoardingPager.getCurrentItem()+1);
                } else {
                    Dexter.withActivity(IntroActivity.this).withPermission(Manifest.permission.ACCESS_FINE_LOCATION).withListener(new PermissionListener() {
                        @Override
                        public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                            startActivity(new Intent(IntroActivity.this, MapActivity.class));
                            finish();
                        }


                        @Override
                        public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                            if (permissionDeniedResponse.isPermanentlyDenied()) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(IntroActivity.this);
                                builder.setTitle("Permission denied").setMessage("Permission to access device location is permanently denied")
                                        .setNegativeButton("Cancel",null)
                                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Intent intent = new Intent();
                                                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                                intent.setData(Uri.fromParts("package", getPackageName(), null));
                                            }
                                        }).show();
                            } else {
                                Toast.makeText(IntroActivity.this, "Permission denied", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                            permissionToken.continuePermissionRequest();
                        }
                    }).check();
                }
            }
        });
    }

    private void setUpBoardingItems() {
        List<IntroBoardingItem> mList = new ArrayList<>();

        mList.add(new IntroBoardingItem("Находите крутые места", "Карта, поиск и фильтры помогут вам найти то самое подходящее место для предстоящей прогулки", R.drawable.nsk_intro2));
        mList.add(new IntroBoardingItem("Делитесь впечатлениями", "Посетив какое-либо место, вы можете поставить ему оценку и написать о нём отзыв.", R.drawable.nsk_intro1));
        mList.add(new IntroBoardingItem("Сохраняйте классные места", "Зацепило место и хотели бы в ближайшее время его посетить - добавьте его в избранное, чтобы точно его не потерять.", R.drawable.theater_3));

        introBoardingAdapter = new IntroBoardingAdapter(mList);
    }

    private void setBoardingIndicators() {
        ImageView[] indicators = new ImageView[introBoardingAdapter.getItemCount()];
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMargins(0,0,26,0);
        for (int i = 0; i < indicators.length; i++) {
            indicators[i] = new ImageView(getApplicationContext());
            indicators[i].setImageDrawable(ContextCompat.getDrawable(
                    getApplicationContext(), R.drawable.indicator_inactive
            ));
            indicators[i].setLayoutParams(layoutParams);
            layoutBoardingIndicators.addView(indicators[i]);
        }
    }

    private void setCurrentIndicator(int index) {
        int childCount = layoutBoardingIndicators.getChildCount();
        for (int i = 0; i < childCount; i++) {
            ImageView imageView = (ImageView) layoutBoardingIndicators.getChildAt(i);
            if (i == index) {
                imageView.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.indicator_active));
            } else {
                imageView.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.indicator_inactive));
            }
        }

        if (index == introBoardingAdapter.getItemCount() - 1) {
            buttonAction.setText("Поехали!");
        } else {
            buttonAction.setText("Свайпай");
        }
    }
}