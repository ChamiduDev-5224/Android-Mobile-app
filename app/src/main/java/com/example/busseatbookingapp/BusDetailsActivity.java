package com.example.busseatbookingapp;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.mail.MessagingException;

public class BusDetailsActivity extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private String busId;
    private int seatCount;
    private TextInputEditText editTextSeatNumber;
    private TextInputEditText editTextCheckingPlace;
    private RecyclerView recyclerViewReservedSeats;
    private MaterialButton buttonReserve;
    private ImageButton backButton;
    private List<Reservation> reservations = new ArrayList<>();
    private ReservedSeatsAdapter adapter;
    private List<Reservation> seatReservations;
    private TextInputEditText editTextReservingDate;
    private TextInputEditText editTextEmail;
    private Calendar calendar;
    private DatePickerDialog datePickerDialog;
    private String busNo;
    private String busRouteDetails;
    private FirebaseAuth mAuth;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_details);


        recyclerViewReservedSeats = findViewById(R.id.recyclerViewReservedSeats);
        recyclerViewReservedSeats.setLayoutManager(new LinearLayoutManager(this));

        reservations = new ArrayList<>();
        adapter = new ReservedSeatsAdapter(reservations);
        recyclerViewReservedSeats.setAdapter(adapter);



        // Initialize UI components
        editTextSeatNumber = findViewById(R.id.editTextSeatNumber);
        editTextCheckingPlace = findViewById(R.id.editTextCheckingPlace);
        buttonReserve = findViewById(R.id.buttonAddSeat);
        backButton = findViewById(R.id.backButtonBusDetails);
        editTextReservingDate = findViewById(R.id.editTextReservingDate);
        editTextEmail = findViewById(R.id.editTextEmail);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        calendar = Calendar.getInstance();

        editTextReservingDate.setOnClickListener(v -> showDatePickerDialog());
        editTextEmail.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                validateEmail();
            }
        });

        buttonReserve.setOnClickListener(v -> reserveSeat());
        findViewById(R.id.buttonConfirmReservation).setOnClickListener(v -> confirmReservation());

        // Retrieve busId from intent
        Intent intent = getIntent();
        busId = intent.getStringExtra("busId");


        if (busId == null) {
            throw new IllegalStateException("Bus ID is missing");
        }

        if (currentUser != null) {
            userId = currentUser.getUid();
        }

        // Initialize DatabaseReference
        databaseReference = FirebaseDatabase.getInstance().getReference("buses").child(busId);

        // Fetch bus details
        fetchBusDetails();

        // Set up listeners
        buttonReserve.setOnClickListener(v -> reserveSeat());
        backButton.setOnClickListener(v -> onBackPressed());
    }

    private void showDatePickerDialog() {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        datePickerDialog = new DatePickerDialog(this,
                (view, year1, month1, dayOfMonth) -> {
                    // Format the selected date and set it to the EditText
                    calendar.set(year1, month1, dayOfMonth);
                    String selectedDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.getTime());
                    editTextReservingDate.setText(selectedDate);
                }, year, month, day);

        datePickerDialog.show();
    }

    private void validateEmail() {
        String email = editTextEmail.getText().toString().trim();
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("Invalid email address");
        }
    }

    private void confirmReservation() {
        new AlertDialog.Builder(this)
                .setTitle("Confirm Reservation")
                .setMessage("Are you sure you want to confirm the reservation?")
                .setPositiveButton(android.R.string.yes, (dialog, which) -> saveReservations())
                .setNegativeButton(android.R.string.no, null)
                .show();
    }

    private void saveReservations() {
        // Reference to the "reserves" node
        DatabaseReference reservesRef = FirebaseDatabase.getInstance().getReference("Reserves");

        for (Reservation reservation : reservations) {
            // Generate a unique key for each reservation
            String reservationId = reservesRef.push().getKey();

            if (reservationId != null) {
                // Create a Reservation object to save
                Map<String, Object> reservationData = new HashMap<>();
                reservationData.put("seatNumber", reservation.getSeatNumber());
                reservationData.put("checkingPlace", reservation.getCheckingPlace());
                reservationData.put("reservedAt", reservation.getReservedAt());
                reservationData.put("busId", reservation.getBusId());
                reservationData.put("userId", reservation.getUserId());
                reservationData.put("status", reservation.getStatus());
                reservationData.put("reservingDate", reservation.getReservingDate());
                reservationData.put("email", reservation.getEmail());

                // Save reservation to Firebase under the "reserves" node
                reservesRef.child(reservationId).setValue(reservationData)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                // Reservation saved successfully
                                Toast.makeText(BusDetailsActivity.this, "Reservations confirmed!", Toast.LENGTH_SHORT).show();
                                reservations.clear();
                                adapter.notifyDataSetChanged();
                                editTextCheckingPlace.setText("");
                                editTextReservingDate.setText("");
                                editTextEmail.setText("");

                                // Send email notification
                                new Thread(() -> {
                                    try {
                                        String userEmail = "featuremelove@gmail.com"; // Replace with your email
                                        String appPassword = "qshn cnmw ekpf voak";
                                        MailSender mailSender = new MailSender(userEmail, appPassword);

                                        String subject = "Reservation Confirmation";
                                        String messageBody = "<html><body>"
                                                + "<h2>Reservation Confirmation</h2>"
                                                + "<p><bold>Your reservation details:</bold></p>"
                                                + "<p><strong>Reservation ID:</strong> " + reservationId + "</p>"
                                                + "<p><strong>Route Details:</strong> " + busRouteDetails + "</p>"
                                                + "<p><strong>Bus No:</strong> " + busNo + "</p>"
                                                + "<p><strong>Seat Number:</strong> " + reservation.getSeatNumber() + "</p>"
                                                + "<p><strong>Checking Place:</strong> " + reservation.getCheckingPlace() + "</p>"
                                                + "<p><strong>Reserving Date:</strong> " + reservation.getReservingDate() + "</p>"
                                                + "<p><strong>Bus ID:</strong> " + reservation.getBusId() + "</p>"
                                                + "<img src='https://res.cloudinary.com/dluhwvkka/image/upload/v1721620345/buslogolight_xck3sb.png' alt='App Logo' width='100' height='100'>"
                                                + "<p>Thank you for choosing our service!</p>"
                                                + "</body></html>";

                                        mailSender.sendEmail(reservation.getEmail(), subject, messageBody);
                                    } catch (MessagingException e) {
                                        e.printStackTrace();
                                    }
                                }).start();
                            } else {
                                // Error saving reservation
                                Toast.makeText(BusDetailsActivity.this, "Failed to save reservation.", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        }
    }

    private void fetchBusDetails() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Bus bus = dataSnapshot.getValue(Bus.class);
                    if (bus != null) {
                        updateUI(bus);
                    } else {
                        Toast.makeText(BusDetailsActivity.this, "Bus data is missing", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(BusDetailsActivity.this, "Bus ID not found in database", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(BusDetailsActivity.this, "Database error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateUI(Bus bus) {
        // Update UI with bus data
        // For example, set text to TextViews
        // Assuming you have TextViews with IDs textViewBusNumber, textViewRoute, etc.
        ((TextView) findViewById(R.id.textViewBusNumber)).setText("Bus No " + bus.getBusNumber());
        ((TextView) findViewById(R.id.distinationView)).setText(bus.getStartForm() + " To " + bus.getEndDestination());
        ((TextView) findViewById(R.id.textViewRoute)).setText("Route No " + bus.getBusRouteNo());
        ((TextView) findViewById(R.id.textViewDepartureTime)).setText("Departure Time " + bus.getDepartureTime());
        ((TextView) findViewById(R.id.textViewArrivalTime)).setText("Arrival Time " + bus.getArrivalTime());
        ((TextView) findViewById(R.id.textViewBusType)).setText("Type " + bus.getBusType());
        ((TextView) findViewById(R.id.seatcountView)).setText("Seat Count " + bus.getSeatCount());
        seatCount = bus.getSeatCount();
        busNo = bus.getBusNumber();
        busRouteDetails = bus.getStartForm() + " To " + bus.getEndDestination() + " - " + bus.getBusRouteNo();
    }

    private void reserveSeat() {
        // Add logic to reserve a seat
        String seatNumber = editTextSeatNumber.getText().toString().trim();
        String checkingPlace = editTextCheckingPlace.getText().toString().trim();
        String reservingDate = editTextReservingDate.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();

        if (seatNumber.isEmpty() || checkingPlace.isEmpty() || reservingDate.isEmpty() || email.isEmpty()) {
            Toast.makeText(BusDetailsActivity.this, "All fields must be filled", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(BusDetailsActivity.this, "Invalid email address", Toast.LENGTH_SHORT).show();
            return;
        }

        int seatNum = Integer.parseInt(seatNumber);
        if (seatNum <= 0 || seatNum > seatCount) {
            Toast.makeText(BusDetailsActivity.this, "Invalid seat number", Toast.LENGTH_SHORT).show();
            return;
        }

        if (isSeatAlreadyReserved(seatNumber)) {
            Toast.makeText(BusDetailsActivity.this, "Seat is already reserved", Toast.LENGTH_SHORT).show();
            return;
        }


        Reservation reservation = new Reservation();
        reservation.setBusId(busId);
        reservation.setUserId(userId);
        reservation.setSeatNumber(Integer.parseInt(seatNumber));
        reservation.setCheckingPlace(checkingPlace);
        reservation.setReservingDate(reservingDate);
        reservation.setStatus(1);
        reservation.setReservedAt(System.currentTimeMillis());
        reservation.setEmail(email);
        reservations.add(reservation);
        adapter.notifyDataSetChanged();
        editTextSeatNumber.setText("");

        Toast.makeText(BusDetailsActivity.this, "Seat reserved successfully", Toast.LENGTH_SHORT).show();
    }


    private boolean isSeatAlreadyReserved(String seatNumber) {
        for (Reservation reservation : reservations) {
            if (reservation.getSeatNumber()==Integer.parseInt(seatNumber)) {
                return true;
            }
        }

        // Check the database for existing reservations
        databaseReference.orderByChild("seatNumber").equalTo(seatNumber).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Reservation reservation = snapshot.getValue(Reservation.class);
                        if (reservation != null && reservation.getReservingDate().equals(editTextReservingDate.getText().toString())) {
                            Toast.makeText(BusDetailsActivity.this, "Seat already reserved in database", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle possible errors
            }
        });

        return false;
    }
}
