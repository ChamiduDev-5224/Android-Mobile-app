package com.example.busseatbookingapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ManageReservation extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ReservationAdapter reservationAdapter;
    private List<Reservation> reservationList;
    private List<Reservation> filteredList;
    private static final int EDIT_RESERVATION_REQUEST = 1; // Request code for editing reservation
    private ImageButton BackButtonManage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_reservation);

        recyclerView = findViewById(R.id.recyclerView);
        BackButtonManage = findViewById(R.id.backButtonManage);
        reservationList = new ArrayList<>();
        filteredList = new ArrayList<>();
        reservationAdapter = new ReservationAdapter(this, filteredList, new ReservationAdapter.OnItemClickListener() {
            @Override
            public void onEditClick(Reservation reservation) {
                Intent intent = new Intent(ManageReservation.this, EditReservationActivity.class);
                intent.putExtra("reservation", reservation);
                startActivityForResult(intent, EDIT_RESERVATION_REQUEST); // Start for result
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(reservationAdapter);

        loadReservations();

        // Back button
        BackButtonManage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ManageReservation.this, RootActivity.class);
                startActivity(intent);
            }
        });

        // Setup SearchView for filtering reservations
        SearchView searchView = findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterReservations(newText.toLowerCase());
                return true;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == EDIT_RESERVATION_REQUEST && resultCode == RESULT_OK) {
            loadReservations(); // Reload reservations when coming back from edit activity
        }
    }


    private void filterReservations(String searchText) {
        if (TextUtils.isEmpty(searchText)) {
            filteredList.clear();
            filteredList.addAll(reservationList);
        } else {
            filteredList.clear();
            for (Reservation res : reservationList) {
                if (res.getBusId().toLowerCase().contains(searchText) ||
                        res.getUserId().toLowerCase().contains(searchText) ||
                        res.getCheckingPlace().toLowerCase().contains(searchText) ||
                        res.getEmail().toLowerCase().contains(searchText) ||
                        Integer.toString(res.getSeatNumber()).toLowerCase().contains(searchText)) {
                    filteredList.add(res);
                }
            }
        }
        reservationAdapter.notifyDataSetChanged();
    }

    private void loadReservations() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Reserves");

        databaseReference.orderByChild("userId").equalTo(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                reservationList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Reservation reservation = snapshot.getValue(Reservation.class);
                    if (reservation != null) {
                        reservation.setReservationId(snapshot.getKey()); // Set the reservation ID
                        reservationList.add(reservation);
                    }
                }
                filteredList.clear();
                filteredList.addAll(reservationList);
                reservationAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle database error
            }
        });
    }
}