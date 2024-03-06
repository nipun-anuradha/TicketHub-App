package com.javainstitute.tickethub;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.Manifest;


import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.javainstitute.tickethub.adapter.CardAdapter;
import com.javainstitute.tickethub.model.CardData;
import com.javainstitute.tickethub.model.Ticket;
import com.javainstitute.tickethub.model.User;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {
    private FirebaseFirestore fireStore;
    private FirebaseStorage storage;
    private FirebaseAuth firebaseAuth;
    static User userObject = new User();

    User user = new User();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //Connection receiver
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        NetworkChangeReceiver mbr = new NetworkChangeReceiver();
        registerReceiver(mbr,intentFilter);
        //Connection receiver



        fireStore = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        FirebaseUser userAuth = firebaseAuth.getCurrentUser();

        fireStore.collection("Users")
                .whereEqualTo("email", userAuth.getEmail())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            QuerySnapshot querySnapshot = task.getResult();
                            if (querySnapshot != null && !querySnapshot.isEmpty()) {
                                DocumentSnapshot logUserDocument = querySnapshot.getDocuments().get(0);

                                userObject = logUserDocument.toObject(User.class);

                            }
                        } else {
                            Log.w("TAG", "Error checking if user exists", task.getException());
                        }
                    }
                });


        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<CardData> cardList = new ArrayList<>();

        CardAdapter adapter = new CardAdapter(this, cardList);
        recyclerView.setAdapter(adapter);

        fireStore.collection("Tickets").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                cardList.clear();
                for (QueryDocumentSnapshot snapshot: task.getResult()){
                    Ticket ticket = snapshot.toObject(Ticket.class);



                    //get ticket owner profile name
                    fireStore.collection("Users")
                            .whereEqualTo("email", ticket.getEmail())
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        QuerySnapshot querySnapshot = task.getResult();
                                        if (querySnapshot != null && !querySnapshot.isEmpty()) {
                                            DocumentSnapshot userDocument = querySnapshot.getDocuments().get(0);
                                            user  = userDocument.toObject(User.class);

                                            CardData cardData = new CardData(
                                                    R.drawable.card,
                                                    ticket.getImage(),
                                                    R.drawable.baseline_heart_broken_24,
                                                    R.drawable.default_pic,
                                                    user.getName(),
                                                    ticket.getId()
                                            );
                                            cardList.add(cardData);
                                            adapter.notifyDataSetChanged();
                                        }
                                    } else {
                                        Log.w("TAG", "Error checking if user exists", task.getException());
                                    }
                                }
                            });
                    //--

                }

            }
        });




        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setSelectedItemId(R.id.bottom_home);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.bottom_home) {
                // Handle home action
                return true;
            } else if (item.getItemId() == R.id.bottom_search) {
                startActivity(new Intent(getApplicationContext(), ActivitySearch.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            } else if (item.getItemId() == R.id.bottom_shop) {
                startActivity(new Intent(getApplicationContext(), ShopActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            } else if (item.getItemId() == R.id.bottom_profile) {
                startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            }
            return false;
        });


    }


    @Override
    public void onBackPressed() {
        // Close the app when the back button is pressed
        finishAffinity();
    }


}