package com.solution.citylogia;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;


public class InfoCardAdapter extends RecyclerView.Adapter<InfoCardAdapter.InfoCardViewHolder>  {

    private List<InfoCardItem> infoCardItems;

    public InfoCardAdapter(List<InfoCardItem> infoCardItems) {
        this.infoCardItems = infoCardItems;
    }

    @NonNull
    @Override
    public InfoCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new InfoCardViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.place_info_add_img, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull InfoCardViewHolder holder, int position) {
        holder.setCardData(infoCardItems.get(position));
    }

    @Override
    public int getItemCount() {
        return infoCardItems.size();
    }

    class InfoCardViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imageSlide;


        InfoCardViewHolder(@NonNull View itemView) {
            super(itemView);

            imageSlide = itemView.findViewById(R.id.image_replace);
        }

        void setCardData(InfoCardItem infoCardItem) {
            try {
                Picasso.get().load(infoCardItem.getUrlCardImage())
                        .placeholder(R.drawable.image_template)
                        .into(imageSlide);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
