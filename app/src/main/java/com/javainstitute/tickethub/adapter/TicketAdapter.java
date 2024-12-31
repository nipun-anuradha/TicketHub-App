package com.javainstitute.tickethub.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.javainstitute.tickethub.R;
import com.javainstitute.tickethub.model.Ticket;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class TicketAdapter extends RecyclerView.Adapter<TicketAdapter.ViewHolder> {
    private ArrayList<Ticket> tickets;
    private FirebaseStorage storage;
    private Context context;

    public TicketAdapter(ArrayList<Ticket> tickets, Context context) {
        this.tickets = tickets;
        this.storage = FirebaseStorage.getInstance();
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.fragment_shop_one_ticket, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Ticket ticket = tickets.get(position);
        holder.title.setText(ticket.getTitle());

        if (holder.image != null) {
            storage.getReference("ticket-images/" + ticket.getImage()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {

                    Picasso.get().load(uri).resize(holder.image.getWidth(), 500).centerCrop().into(holder.image);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return tickets.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        TextView title;
        ImageView image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.ticketTitle);
            image = itemView.findViewById(R.id.ticketImage);
        }
    }
}

