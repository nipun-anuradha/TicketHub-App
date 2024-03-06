package com.javainstitute.tickethub;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class ProfileActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firestore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        //update shop name
        findViewById(R.id.updateBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText newNameEditText = findViewById(R.id.newShopName);
                String newName = newNameEditText.getText().toString();
                if(!TextUtils.isEmpty(newName)) {

                    firestore.collection("Users")
                            .whereEqualTo("email", currentUser.getEmail())
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        QuerySnapshot querySnapshot = task.getResult();
                                        if (querySnapshot != null && !querySnapshot.isEmpty()) {
                                            DocumentSnapshot userDocument = querySnapshot.getDocuments().get(0);
                                            String userId = userDocument.getId();

                                            // Now update the seller status to true
                                            firestore.collection("Users")
                                                    .document(userId)
                                                    .update("name", newName)
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            // Update successful
                                                            TextView oldName = findViewById(R.id.shopNameTextView);
                                                            oldName.setText(newName);
                                                            newNameEditText.setText("");
                                                            HomeActivity.userObject.setName(newName);

                                                            Toast.makeText(ProfileActivity.this, "Account Name update successful", Toast.LENGTH_SHORT).show();
                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            // Update failed
                                                            Log.w("TAG", "Error updating seller status", e);
                                                        }
                                                    });
                                        }
                                    } else {
                                        Log.w("TAG", "Error checking if user exists", task.getException());
                                    }
                                }
                            });
                }else{
                    Toast.makeText(ProfileActivity.this,"Update name field is empty!",Toast.LENGTH_SHORT).show();
                }
            }
        });
        //----



        TextView name = findViewById(R.id.shopNameTextView);
        TextView email = findViewById(R.id.emailTextView);
        name.setText(HomeActivity.userObject.getName());
        email.setText(HomeActivity.userObject.getEmail());



        findViewById(R.id.button0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();

                Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setSelectedItemId(R.id.bottom_profile);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.bottom_home) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            }

            if (item.getItemId() == R.id.bottom_search) {
                startActivity(new Intent(getApplicationContext(), ActivitySearch.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            }

            if (item.getItemId() == R.id.bottom_shop) {
                startActivity(new Intent(getApplicationContext(), ShopActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            }

            if (item.getItemId() == R.id.bottom_profile) {
                return true;
            }

            return false;
        });

    }
}