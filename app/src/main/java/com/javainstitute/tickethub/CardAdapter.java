package com.javainstitute.tickethub;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.CardViewHolder> {

    private List<Integer> dataList; // Assuming your data is a list of drawable resource IDs

    public CardAdapter(List<Integer> dataList) {
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card, parent, false);
        return new CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
        int drawableResourceId = dataList.get(position);

        // Bind the drawable resource ID to the ImageView
        holder.imageView.setImageResource(drawableResourceId);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public static class CardViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public CardViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }
}
