package com.javainstitute.tickethub;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.javainstitute.tickethub.model.User;

public class CreateShop extends Fragment {
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firestore;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_create_shop, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        checkUserStatus();

        view.findViewById(R.id.openShop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProgressDialog dialog = new ProgressDialog(CreateShop.this.requireContext());
                dialog.setMessage("Creating your shop...");
                dialog.setCancelable(false);
                dialog.show();


                getParentFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainerShop, new ViewShop())
                        .commit();


                //update user seller status
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
                                                .update("seller", true)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        // Update successful
                                                        dialog.dismiss();
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

            }
        });
    }

    private void checkUserStatus() {
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {
            firestore.collection("Users")
                    .whereEqualTo("email", currentUser.getEmail())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                QuerySnapshot querySnapshot = task.getResult();
                                if (querySnapshot != null && !querySnapshot.isEmpty()) {
                                    for (QueryDocumentSnapshot document : querySnapshot) {
                                        User user = document.toObject(User.class);

                                        if (user != null && user.isSeller()) {
                                            // User is a seller, load ViewShop fragment
                                            getParentFragmentManager().beginTransaction()
                                                    .replace(R.id.fragmentContainerShop, new ViewShop())
                                                    .addToBackStack(null)
                                                    .commit();
                                        } else {
                                            // User is not a seller, handle accordingly
                                            // For example, you may want to display a message or perform another action
                                        }
                                    }
                                }
                            } else {
                                // Handle the error
                            }
                        }
                    });
        }
    }


}