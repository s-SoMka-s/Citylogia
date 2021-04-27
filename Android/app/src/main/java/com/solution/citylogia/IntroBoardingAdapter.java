package com.solution.citylogia;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;

import java.util.List;


public class IntroBoardingAdapter extends RecyclerView.Adapter<IntroBoardingAdapter.IntroBoardingViewHolder>  {

    private List<IntroBoardingItem> introBoardingItems;

    public IntroBoardingAdapter(List<IntroBoardingItem> introBoardingItems) {
        this.introBoardingItems = introBoardingItems;
    }

    @NonNull
    @Override
    public IntroBoardingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new IntroBoardingViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.intro_layout_screen, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull IntroBoardingViewHolder holder, int position) {
        holder.setBoardingData(introBoardingItems.get(position));
    }

    @Override
    public int getItemCount() {
        return introBoardingItems.size();
    }

    class IntroBoardingViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageSlide;
        private TextView title;
        private TextView description;

        IntroBoardingViewHolder(@NonNull View itemView) {
            super(itemView);

            imageSlide = itemView.findViewById(R.id.image_background);
            title = itemView.findViewById(R.id.title_intro);
            description = itemView.findViewById(R.id.text_intro);
        }

        void setBoardingData(IntroBoardingItem introBoardingItem) {
            title.setText(introBoardingItem.getTitle());
            description.setText(introBoardingItem.getDescription());
            imageSlide.setImageResource(introBoardingItem.getScreenImg());
        }
    }
}
