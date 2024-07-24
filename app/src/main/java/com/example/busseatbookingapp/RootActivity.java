package com.example.busseatbookingapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;

public class RootActivity extends AppCompatActivity {

    CardView addBusCard;
    CardView logOutCard;
    CardView reserveCard;
    CardView aboutUsCard;
    CardView manageCard;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_root); // Ensure this is the correct layout file name

        // Initialize the CardView
        addBusCard = findViewById(R.id.addbuscard);
        logOutCard = findViewById(R.id.logoutcardid);
        reserveCard = findViewById(R.id.reserveCard);
        aboutUsCard = findViewById(R.id.aboutUsCard);
        manageCard = findViewById(R.id.manageCardView);

        // Set the click listener for addBusCard
        addBusCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RootActivity.this, AddBusActivity.class);
                startActivity(intent);
            }
        });


        reserveCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RootActivity.this, ReserveBusActivity.class);
                startActivity(intent);
            }
        });

        // move to about us screen
        aboutUsCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RootActivity.this, AboutActivity.class);
                startActivity(intent);
            }
        });

        //move to mange screen

        manageCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RootActivity.this, ManageReservation.class);
                startActivity(intent);
            }
        });

        // Set click listener for logOutCard
        logOutCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showLogoutConfirmationDialog();
            }
        });
    }

    private void showLogoutConfirmationDialog() {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this, R.style.AlertDialogTheme);
        builder.setTitle("Logout Confirmation")
                .setMessage("Are you sure you want to logout?")
                .setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Proceed with logout
                        logoutUser();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Cancel action, do nothing
                    }
                })
                .setCancelable(false)
                .setIcon(ContextCompat.getDrawable(this, R.drawable.buslogolight));

        builder.show();
    }



    private void logoutUser() {
        FirebaseAuth.getInstance().signOut();
        // Navigate to LoginActivity or any other appropriate screen after logout
        Intent intent = new Intent(RootActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Clear back stack
        startActivity(intent);
        finish(); // Finish current activity
    }
}
