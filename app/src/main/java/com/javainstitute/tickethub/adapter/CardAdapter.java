package com.javainstitute.tickethub.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.javainstitute.tickethub.ProfileActivity;
import com.javainstitute.tickethub.R;
import com.javainstitute.tickethub.TicketDetailActivity;
import com.javainstitute.tickethub.model.CardData;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.CardViewHolder> {
    private FirebaseStorage storage;

    private Context context;
    private List<CardData> cardList; // Assuming you have a data class named CardData

    public CardAdapter(Context context, List<CardData> cardList) {
        this.storage = FirebaseStorage.getInstance();
        this.context = context;
        this.cardList = cardList;
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.card, parent, false);
        return new CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
        CardData card = cardList.get(position);

        // Set values to the views
        holder.imageView.setImageResource(card.getImageResource());
        holder.buyTicketButton.setText("Open Ticket");
        holder.starImageView.setImageResource(card.getImageResourceStar());
        holder.iconImageView.setImageResource(card.getImageResourceIcon());
        holder.profileNameTextView.setText(card.getProfileName());

        if (holder.mainImageView != null) {
            storage.getReference("ticket-images/" + card.getImageResourceMain()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {

                    Picasso.get().load(uri).resize(holder.mainImageView.getWidth(), holder.mainImageView.getHeight()).into(holder.mainImageView);
                }
            });
        }

        Button button = holder.buyTicketButton;
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, TicketDetailActivity.class);
                intent.putExtra("ticketId",card.getTicketId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return cardList.size();
    }

    public class CardViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        Button buyTicketButton;
        ImageView starImageView;
        ImageView iconImageView;
        TextView profileNameTextView;
        ImageView mainImageView;

        public CardViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageView);
            buyTicketButton = itemView.findViewById(R.id.buyTicket);
            starImageView = itemView.findViewById(R.id.imageViewStar);
            iconImageView = itemView.findViewById(R.id.imageViewIcon);
            profileNameTextView = itemView.findViewById(R.id.profileName);
            mainImageView = itemView.findViewById(R.id.imageViewMain);
        }
    }
}
