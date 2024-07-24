package com.example.busseatbookingapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ReserveBusActivity extends AppCompatActivity {

    private RecyclerView recyclerViewBuses;
    private BusAdapter busAdapter;
    private List<Bus> busList;
    private List<Bus> filteredBusList;
    private DatabaseReference databaseReference;
    private ImageButton backButtonBusReserve;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserve_bus);

        // Initialize views
        recyclerViewBuses = findViewById(R.id.recyclerViewBuses);
        backButtonBusReserve = findViewById(R.id.backButtonBusReserve);

        // Setup RecyclerView
        busList = new ArrayList<>();
        filteredBusList = new ArrayList<>();
        busAdapter = new BusAdapter(this, filteredBusList, new BusAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Bus bus) {
                showBusDetails(bus);
            }
        });
        recyclerViewBuses.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewBuses.setAdapter(busAdapter);

        // Setup Firebase database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("buses");

        // Load all buses initially
        loadAllBuses();

        // Setup SearchView for filtering buses
        SearchView searchView = findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterBuses(newText.toLowerCase());
                return true;
            }
        });

        // Back button event
        backButtonBusReserve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate back to RootActivity
                Intent intent = new Intent(ReserveBusActivity.this, RootActivity.class);
                startActivity(intent);
            }
        });
    }

    private void loadAllBuses() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                busList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Bus bus = snapshot.getValue(Bus.class);
                    if (bus != null) {
                        busList.add(bus);
                    }
                }
                // Initially, display all buses
                filteredBusList.clear();
                filteredBusList.addAll(busList);
                busAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ReserveBusActivity.this, "Failed to load buses: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void filterBuses(String searchText) {
        if (TextUtils.isEmpty(searchText)) {
            // If the search text is empty, show all buses
            filteredBusList.clear();
            filteredBusList.addAll(busList);
        } else {
            // Filter based on the search text
            filteredBusList.clear();
            for (Bus bus : busList) {
                if (bus.getBusNumber().toLowerCase().contains(searchText) ||
                        bus.getBusRouteNo().toLowerCase().contains(searchText) ||
                        bus.getStartForm().toLowerCase().contains(searchText) ||
                        bus.getEndDestination().toLowerCase().contains(searchText) ||
                        bus.getDepartureTime().toLowerCase().contains(searchText) ||
                        bus.getArrivalTime().toLowerCase().contains(searchText) ||
                        bus.getBusType().toLowerCase().contains(searchText) ||
                        bus.getRunningCondition().toLowerCase().contains(searchText)) {
                    filteredBusList.add(bus);
                }
            }
        }
        busAdapter.notifyDataSetChanged();
    }

    private void showBusDetails(Bus bus) {
        Intent intent = new Intent(ReserveBusActivity.this, BusDetailsActivity.class);
        intent.putExtra("busNumber", bus.getBusNumber());
        intent.putExtra("route", bus.getBusRouteNo());
        intent.putExtra("departureTime", bus.getDepartureTime());
        intent.putExtra("arrivalTime", bus.getArrivalTime());
        intent.putExtra("busType", bus.getBusType());
        intent.putExtra("busId", bus.getBusId());
        startActivity(intent);
    }
}
