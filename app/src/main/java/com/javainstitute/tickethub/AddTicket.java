package com.javainstitute.tickethub;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.javainstitute.tickethub.model.Ticket;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.UUID;


public class AddTicket extends Fragment implements TimePickerFragment.OnTimeSetListener, DatePickerFragment.OnDateSetListener{

    private TextView time;
    private TextView date;
    private ImageButton imageButton;
    private Uri imagePath;
    private TextView ticketId;

    private FirebaseStorage storage;
    private FirebaseFirestore firestore;
    private FirebaseAuth firebaseAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_ticket, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        firestore = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        FirebaseApp.initializeApp(requireContext());

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();

        time = view.findViewById(R.id.timeTextView);
        date = view.findViewById(R.id.dateTextView);
        imageButton = view.findViewById(R.id.imageButton);


        ticketId = view.findViewById(R.id.ticketId);
        ticketId.setText(UUID.randomUUID().toString());

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);

                activityResultLauncher.launch(Intent.createChooser(intent,"Select Image"));
            }
        });


        view.findViewById(R.id.pickTimeButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerFragment timePickerFragment = new TimePickerFragment();
                timePickerFragment.setOnTimeSetListener(AddTicket.this);
                timePickerFragment.show(requireActivity().getSupportFragmentManager(), "timePicker");
            }
        });

        view.findViewById(R.id.pickDateButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerFragment datePickerFragment = new DatePickerFragment();
                datePickerFragment.setOnDateSetListener(AddTicket.this);
                datePickerFragment.show(requireActivity().getSupportFragmentManager(), "datePicker");
            }
        });


        view.findViewById(R.id.addBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View viewbtn) {
                ticketId = view.findViewById(R.id.ticketId);
                EditText ticketName = view.findViewById(R.id.ticketName);
                TextView time = view.findViewById(R.id.timeTextView);
                TextView date = view.findViewById(R.id.dateTextView);
                EditText place = view.findViewById(R.id.place);
                EditText location = view.findViewById(R.id.location);
                EditText price = view.findViewById(R.id.price);
                EditText qty = view.findViewById(R.id.qty);
                String imageId = UUID.randomUUID().toString();

                if (TextUtils.isEmpty(ticketName.getText().toString()) ||
                        time.getText().toString().equals("00 : 00") ||
                        date.getText().toString().equals("DD-MM-YY") ||
                        TextUtils.isEmpty(place.getText().toString()) ||
                        TextUtils.isEmpty(price.getText().toString()) ||
                        TextUtils.isEmpty(qty.getText().toString())
                ) {
                    Toast.makeText(requireContext().getApplicationContext(), "Please check details", Toast.LENGTH_SHORT).show();
                }else{
                    String Location;
                    if (TextUtils.isEmpty(location.getText().toString())){
                        Location = null;
                    }else{
                        Location = location.getText().toString();
                    }

                    Ticket ticket = new Ticket(imageId,
                            ticketId.getText().toString(),
                            ticketName.getText().toString(),
                            time.getText().toString(),
                            date.getText().toString(),
                            place.getText().toString(),
                            Location,
                            price.getText().toString(),
                            Integer.parseInt(qty.getText().toString()),
                            user.getEmail());


                    ProgressDialog dialog = new ProgressDialog(AddTicket.this.requireContext());
                    dialog.setMessage("Add new Ticket...");
                    dialog.setCancelable(false);
                    dialog.show();

                    firestore.collection("Tickets").add(ticket).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            if(imagePath != null){
                                dialog.setMessage("Uploading Ticket...");

                                StorageReference reference = storage.getReference("ticket-images").child(imageId);
                                reference.putFile(imagePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        dialog.dismiss();
                                        getParentFragmentManager().beginTransaction()
                                                .replace(R.id.fragmentContainerShop, new ViewShop())
                                                .addToBackStack(null)
                                                .commit();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        dialog.dismiss();
                                        Toast.makeText(getActivity().getApplicationContext(), e.getMessage(),Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                                        double progress = (100.0 * snapshot.getBytesTransferred()) / snapshot.getBytesTransferred();
                                        dialog.setMessage("Uploading "+(int)progress+"%");
                                    }
                                });
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            dialog.dismiss();
                            Toast.makeText(requireContext().getApplicationContext(), e.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    });
                }


            }
        });


    }

    @Override
    public void onTimeSet(int hourOfDay, int minute) {
        String formattedTime = String.format("%02d:%02d", hourOfDay, minute);
        time.setText(formattedTime);
    }

    @Override
    public void onDateSet(int year, int month, int day) {
        String formattedDate = String.format("%02d-%02d-%d", day, month + 1, year); // Month is 0-indexed
        date.setText(formattedDate);
    }

    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == Activity.RESULT_OK){
                        imagePath = result.getData().getData();

                        Picasso.get().load(imagePath).resize(imageButton.getWidth(),imageButton.getHeight()).centerCrop().into(imageButton);

                    }
                }
            }
    );

}
