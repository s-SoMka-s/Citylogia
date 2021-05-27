package com.solution.citylogia;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PlaceInsideAdapter extends RecyclerView.Adapter<PlaceInsideAdapter.PlaceInsideViewHolder> {
    private List<PlaceInsideItem> placeInsideItems;

    public PlaceInsideAdapter(List<PlaceInsideItem> items)
    {
        this.placeInsideItems = items;
    }

    @NonNull
    @Override
    public PlaceInsideAdapter.PlaceInsideViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PlaceInsideViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.place_inside_layout_screen, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull PlaceInsideAdapter.PlaceInsideViewHolder holder, int position) {
        holder.setBoardingData(placeInsideItems.get(position));
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class PlaceInsideViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageSlide;
        private TextView title;
        private TextView description;

        PlaceInsideViewHolder(@NonNull View itemView) {
            super(itemView);

            imageSlide = itemView.findViewById(R.id.place_inside_background);
        }

        void setBoardingData(PlaceInsideItem placeInsideItem) {
            imageSlide.setImageResource(placeInsideItem.getScreenImg());
        }
    }
}
