package com.example.busseatbookingapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import javax.mail.MessagingException;

public class EditReservationActivity extends AppCompatActivity {

    private TextView textViewBusNumber, distinationView, textViewRoute, textViewDepartureTime, textViewArrivalTime, textViewBusType, seatcountView;
    private EditText editTextSeatNumber, editTextCheckingPlace, editTextReservingDate, editTextEmail;
    private MaterialButton buttonConfirmReservation;
    private Reservation reservation;
    private Switch switchStatus;
    private String busNo,busRouteDetails;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_reservation);

        // Initialize UI elements
        textViewBusNumber = findViewById(R.id.textViewBusNumber);
        distinationView = findViewById(R.id.distinationView);
        textViewRoute = findViewById(R.id.textViewRoute);
        textViewDepartureTime = findViewById(R.id.textViewDepartureTime);
        textViewArrivalTime = findViewById(R.id.textViewArrivalTime);
        textViewBusType = findViewById(R.id.textViewBusType);
        seatcountView = findViewById(R.id.seatcountView);
        switchStatus = findViewById(R.id.switchStatus);

        editTextSeatNumber = findViewById(R.id.editTextSeatNumber);
        editTextCheckingPlace = findViewById(R.id.editTextCheckingPlace);
        editTextReservingDate = findViewById(R.id.editTextReservingDate);
        editTextEmail = findViewById(R.id.editTextEmail);

        buttonConfirmReservation = findViewById(R.id.buttonConfirmReservation);

        // Get the reservation data from the intent
        reservation = (Reservation) getIntent().getSerializableExtra("reservation");

        // Populate the fields with reservation data
        if (reservation != null) {
            editTextSeatNumber.setText(String.valueOf(reservation.getSeatNumber()));
            editTextCheckingPlace.setText(reservation.getCheckingPlace());
            editTextReservingDate.setText(reservation.getReservingDate());
            editTextEmail.setText(reservation.getEmail());
            switchStatus.setChecked(reservation.getStatus() == 1);


            // Fetch and display bus details
            fetchBusDetails(reservation.getBusId());
        }

        buttonConfirmReservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showConfirmationDialog();
            }
        });

        // Back button functionality
        ImageButton backButtonBusDetails = findViewById(R.id.backButtonBusDetails);
        backButtonBusDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Close this activity and go back to the previous one
            }
        });
    }

    private void fetchBusDetails(String busId) {
        DatabaseReference busRef = FirebaseDatabase.getInstance().getReference("buses").child(busId);
        busRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Bus bus = dataSnapshot.getValue(Bus.class);
                if (bus != null) {
                    textViewBusNumber.setText("Bus Number: " + bus.getBusNumber());
                    distinationView.setText("Destination: " + bus.getEndDestination());
                    textViewRoute.setText("Route: " + bus.getBusRouteNo());
                    textViewDepartureTime.setText("Departure Time: " + bus.getDepartureTime());
                    textViewArrivalTime.setText("Arrival Time: " + bus.getArrivalTime());
                    textViewBusType.setText("Bus Type: " + bus.getBusType());
                    seatcountView.setText("Seat Count: " + bus.getSeatCount());
                    busNo = bus.getBusNumber();
                    busRouteDetails = bus.getStartForm() + " To " + bus.getEndDestination() + " - " + bus.getBusRouteNo();

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle database error
            }
        });
    }

    private void showConfirmationDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Confirm Update")
                .setMessage("Are you sure you want to update this reservation?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        updateReservation();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }


    private void updateReservation() {
        String newSeatNumber = editTextSeatNumber.getText().toString();
        String newCheckingPlace = editTextCheckingPlace.getText().toString();
        String newReservingDate = editTextReservingDate.getText().toString();
        String newEmail = editTextEmail.getText().toString();
        int status = switchStatus.isChecked() ? 1 : 0;

        String reservationId = reservation.getReservationId();
        if (reservationId != null) {
            DatabaseReference reservationRef = FirebaseDatabase.getInstance().getReference("Reserves").child(reservationId);

            reservationRef.child("seatNumber").setValue(Integer.parseInt(newSeatNumber));
            reservationRef.child("checkingPlace").setValue(newCheckingPlace);
            reservationRef.child("reservingDate").setValue(newReservingDate);
            reservationRef.child("status").setValue(status);
            reservationRef.child("email").setValue(newEmail)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(EditReservationActivity.this, "Reservation updated successfully", Toast.LENGTH_SHORT).show();
                            setResult(RESULT_OK); // Set result code
                            finish(); // Finish the activity

                            // Send email notification
                            new Thread(() -> {
                                try {
                                    String subject;
                                    if(status==1){
                                        subject = "Reservation Updated";
                                    } else {
                                        subject = "Reservation Cancelled";

                                    }
                                    String userEmail = "featuremelove@gmail.com"; // Replace with your email
                                    String appPassword = "qshn cnmw ekpf voak";
                                    MailSender mailSender = new MailSender(userEmail, appPassword);

                                    String messageBody = "<html><body>"
                                            + "<h2>"+"Your "+subject+"</h2>"
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
                            Toast.makeText(EditReservationActivity.this, "Failed to update reservation", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

}