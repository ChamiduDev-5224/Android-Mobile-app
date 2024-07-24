package com.example.busseatbookingapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class AboutActivity extends AppCompatActivity {
    private TextView textViewAppName, textViewVersion, textViewDevelopers, textViewDescription, textViewFeatures, textViewPurpose;
    private ImageButton backButtonAboutUs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_about);

        textViewAppName = findViewById(R.id.textViewAppName);
        textViewVersion = findViewById(R.id.textViewVersion);
        textViewDevelopers = findViewById(R.id.textViewDevelopers);
        textViewDescription = findViewById(R.id.textViewDescription);
        textViewFeatures = findViewById(R.id.textViewFeatures);
        textViewPurpose = findViewById(R.id.textViewPurpose);
        backButtonAboutUs = findViewById(R.id.backButtonAboutUs);
        // Set app details
        textViewAppName.setText("SeatReservePlus");
        textViewVersion.setText("Version: 1.0.0");
        textViewDevelopers.setText("Developed by: Chamidu Ravihara");
        textViewDescription.setText("SeatReservePlus is your ultimate solution for reserving bus seats quickly and efficiently. Our app offers a seamless experience for booking seats on your favorite routes.");
        textViewFeatures.setText("Features:\n- Easy seat reservation\n- Real-time bus schedules\n- User-friendly interface\n- Secure payment options\n- Reservation history\n- Notification alerts for upcoming trips");
        textViewPurpose.setText("Purpose:\nOur goal is to simplify bus travel for everyone. With SeatReservePlus, you can avoid the hassle of last-minute bookings and secure your seat well in advance, ensuring a comfortable journey.");
  //back button event
        backButtonAboutUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AboutActivity.this,RootActivity.class);
                startActivity(intent);
            }
        });

    }
}