package com.example.busseatbookingapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class BusDetailsActivity extends AppCompatActivity {
    private TextView textViewBusNumber, textViewRoute, textViewDepartureTime, textViewArrivalTime, textViewBusType;
    private Button buttonReserve;
    private ImageButton backButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_details);

        textViewBusNumber = findViewById(R.id.textViewBusNumber);
        textViewRoute = findViewById(R.id.textViewRoute);
        textViewDepartureTime = findViewById(R.id.textViewDepartureTime);
        textViewArrivalTime = findViewById(R.id.textViewArrivalTime);
        textViewBusType = findViewById(R.id.textViewBusType);
        buttonReserve = findViewById(R.id.buttonReserve);
        backButton = findViewById(R.id.backButtonBusDetails);

        // Retrieve bus details passed from previous activity
        Intent intent = getIntent();
        if (intent != null) {
            String busNumber = intent.getStringExtra("busNumber");
            String route = intent.getStringExtra("route");
            String departureTime = intent.getStringExtra("departureTime");
            String arrivalTime = intent.getStringExtra("arrivalTime");
            String busType = intent.getStringExtra("busType");

            // Set retrieved data to TextViews
            textViewBusNumber.setText("Bus Number: " + busNumber);
            textViewRoute.setText("Route: " + route);
            textViewDepartureTime.setText("Departure Time: " + departureTime);
            textViewArrivalTime.setText("Arrival Time: " + arrivalTime);
            textViewBusType.setText("Bus Type: " + busType);
        }

        buttonReserve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Implement reservation logic here if needed
                reserveSeat();
            }
        });

        //back button event
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BusDetailsActivity.this, ReserveBusActivity.class);
                startActivity(intent);
            }
        });


    }

    private void reserveSeat() {
        // Implement reservation logic if needed
        Toast.makeText(this, "Seat Reserved!", Toast.LENGTH_SHORT).show();
    }
}
