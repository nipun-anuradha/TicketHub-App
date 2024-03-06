package com.javainstitute.tickethub;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.javainstitute.tickethub.adapter.TicketAdapter;
import com.javainstitute.tickethub.model.Ticket;

import java.util.ArrayList;

public class ViewShop extends Fragment {

    private FirebaseFirestore fireStore;
    private FirebaseStorage storage;
    private ArrayList<Ticket> tickets;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_view_shop, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fireStore = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();

        tickets = new ArrayList<>();


        //--------------------
        view.findViewById(R.id.addTicketBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainerShop, new AddTicket())
                        .addToBackStack(null)
                        .commit();
            }
        });
        //------------------

        RecyclerView ticketView = view.findViewById(R.id.shopTicketView);

        TicketAdapter ticketAdapter = new TicketAdapter(tickets, getActivity().getApplicationContext());

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireActivity().getApplicationContext());
        ticketView.setLayoutManager(linearLayoutManager);

        ticketView.setAdapter(ticketAdapter);

        fireStore.collection("Tickets").whereEqualTo("email",HomeActivity.userObject.getEmail()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                tickets.clear();
                for (QueryDocumentSnapshot snapshot: task.getResult()){
                    Ticket ticket = snapshot.toObject(Ticket.class);
                    tickets.add(ticket);
                }

                ticketAdapter.notifyDataSetChanged();
            }
        });



    }




}
