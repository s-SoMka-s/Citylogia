
package com.solution.citylogia;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

public class IntroActivity extends AppCompatActivity {

    private IntroBoardingAdapter introBoardingAdapter;
    private LinearLayout layoutBoardingIndicators;
    private Button buttonAction;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        layoutBoardingIndicators = findViewById(R.id.layoutBoardingIndicators);
        buttonAction = findViewById(R.id.button);

        setUpBoardingItems();

        ViewPager2 introBoardingPager = findViewById(R.id.screen_viewpager);
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
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    finish();
                }
            }
        });
    }

    private void setUpBoardingItems() {
        List<IntroBoardingItem> mList = new ArrayList<>();

        mList.add(new IntroBoardingItem("Умная рекомендация мест", "Карточки с предлагаемыми интересными местами создаются на основе ваших предпочтений и истории посещений мест.", R.drawable.nsu_1));
        mList.add(new IntroBoardingItem("Умный поиск по карте", "Amet minim mollit non deserunt ullamco est sit aliqua dolor do amet sint. Velit officia consequat duis enim velit mollit.", R.drawable.park_2));
        mList.add(new IntroBoardingItem("Взаимодействуйте со своими друзьями", "Вы можете добавлять друзей, просматривать их недавние посещения, а также общаться в мессенджере и отслеживать их текущее местоположение.", R.drawable.theater_3));

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