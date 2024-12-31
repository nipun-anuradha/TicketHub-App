package com.javainstitute.tickethub;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.javainstitute.tickethub.model.Ticket;
import com.squareup.picasso.Picasso;

public class TicketDetailActivity extends AppCompatActivity {

    private FirebaseFirestore firestore;
    private FirebaseStorage storage;

    Ticket ticket = new Ticket();

    private String ticketId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_detail);

        firestore = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();

        ticketId = getIntent().getStringExtra("ticketId");


        //location map open
        findViewById(R.id.locationView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TicketDetailActivity.this,MapActivity.class);
                intent.putExtra("ticketId", ticketId);
                intent.putExtra("latLang",ticket.getLocation());
                startActivity(intent);
                finish();
            }
        });
        //--
        ImageView img = findViewById(R.id.detail_img);
        TextView id = findViewById(R.id.detail_id);
        TextView title = findViewById(R.id.detail_title);
        TextView date = findViewById(R.id.detail_date);
        TextView time = findViewById(R.id.detail_time);
        TextView venue = findViewById(R.id.detail_venue);
        TextView price = findViewById(R.id.detail_price);


        firestore.collection("Tickets")
                .whereEqualTo("id", ticketId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            QuerySnapshot querySnapshot = task.getResult();
                            if (querySnapshot != null && !querySnapshot.isEmpty()) {

                                DocumentSnapshot documentSnapshot = querySnapshot.getDocuments().get(0);
                                ticket = documentSnapshot.toObject(Ticket.class);

                                storage.getReference("ticket-images/" + ticket.getImage()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        Picasso.get().load(uri).resize(img.getWidth(), img.getHeight()).into(img);
                                    }
                                });

                                id.setText(ticket.getId());
                                title.setText(ticket.getTitle());
                                date.setText(ticket.getDate());
                                time.setText(ticket.getTime());
                                venue.setText(ticket.getPlace());
                                price.setText(ticket.getPrice());

                            }
                        } else {
                            Log.w("TAG", "Error checking if user exists", task.getException());
                        }
                    }
                });
    }
}