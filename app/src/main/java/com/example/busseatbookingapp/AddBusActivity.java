package com.example.busseatbookingapp;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class AddBusActivity extends AppCompatActivity {

    private TextInputEditText startFormEditText, endDestinationEditText, busRouteNoEditText, busNumberEditText, departureTimeEditText, arrivalTimeEditText, seatCountEditText;
    private Spinner busTypeSpinner, runningConditionSpinner;
    private MaterialButton submitButton;
    private ImageButton backButtonAddBus;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bus);

        databaseReference = FirebaseDatabase.getInstance().getReference("buses");

        startFormEditText = findViewById(R.id.startForm);
        endDestinationEditText = findViewById(R.id.endDestination);
        busRouteNoEditText = findViewById(R.id.busRouteNo);
        busNumberEditText = findViewById(R.id.busNumber);
        departureTimeEditText = findViewById(R.id.departureTime);
        arrivalTimeEditText = findViewById(R.id.arrivalTime);
        seatCountEditText = findViewById(R.id.seatCount);
        busTypeSpinner = findViewById(R.id.busType);
        runningConditionSpinner = findViewById(R.id.runningCondition);
        submitButton = findViewById(R.id.submitButton);
        backButtonAddBus = findViewById(R.id.backButtonAddBus);
        // Time Picker for Departure Time
        departureTimeEditText.setOnClickListener(view -> showTimePickerDialog(departureTimeEditText));
        // Time Picker for Arrival Time
        arrivalTimeEditText.setOnClickListener(view -> showTimePickerDialog(arrivalTimeEditText));

        submitButton.setOnClickListener(view -> {
            if (validateInputs()) {
                addBusToDatabase();
            }
        });

        //BACK for dashboard
        backButtonAddBus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddBusActivity.this,RootActivity.class);
                startActivity(intent);
            }
        });
    }

    private void showTimePickerDialog(TextInputEditText editText) {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(AddBusActivity.this,
                (timePicker, selectedHour, selectedMinute) -> editText.setText(String.format("%02d:%02d", selectedHour, selectedMinute)),
                hour, minute, true);
        timePickerDialog.show();
    }

    private boolean validateInputs() {
        if (TextUtils.isEmpty(startFormEditText.getText())) {
            showErrorSnackbar("Please enter the start location");
            return false;
        }
        if (TextUtils.isEmpty(endDestinationEditText.getText())) {
            showErrorSnackbar("Please enter the end destination");
            return false;
        }
        if (TextUtils.isEmpty(busRouteNoEditText.getText())) {
            showErrorSnackbar("Please enter the bus route number");
            return false;
        }
        if (TextUtils.isEmpty(busNumberEditText.getText())) {
            showErrorSnackbar("Please enter the bus number");
            return false;
        }
        if (TextUtils.isEmpty(departureTimeEditText.getText())) {
            showErrorSnackbar("Please enter the departure time");
            return false;
        }
        if (TextUtils.isEmpty(arrivalTimeEditText.getText())) {
            showErrorSnackbar("Please enter the arrival time");
            return false;
        }
        if (TextUtils.isEmpty(seatCountEditText.getText())) {
            showErrorSnackbar("Please enter the bus seat count");
            return false;
        }
        if (busTypeSpinner.getSelectedItemPosition() == 0) {
            showErrorSnackbar("Please select the bus type");
            return false;
        }
        if (runningConditionSpinner.getSelectedItemPosition() == 0) {
            showErrorSnackbar("Please select the running condition");
            return false;
        }
        return true;
    }

    private void showErrorSnackbar(String message) {
        Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG).show();
    }

    private void addBusToDatabase() {
        String startForm = startFormEditText.getText().toString().trim();
        String endDestination = endDestinationEditText.getText().toString().trim();
        String busRouteNo = busRouteNoEditText.getText().toString().trim();
        String busNumber = busNumberEditText.getText().toString().trim();
        String departureTime = departureTimeEditText.getText().toString().trim();
        String arrivalTime = arrivalTimeEditText.getText().toString().trim();
        String busType = busTypeSpinner.getSelectedItem().toString();
        String runningCondition = runningConditionSpinner.getSelectedItem().toString();
        int seatCount = Integer.parseInt(seatCountEditText.getText().toString().trim());

        String busId = databaseReference.push().getKey();

        Bus bus = new Bus(busId, startForm, endDestination, busRouteNo, busNumber, departureTime, arrivalTime, busType, runningCondition,  seatCount);

        if (busId != null) {
            databaseReference.child(busId).setValue(bus).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    showSuccessSnackbar("Bus added successfully");
                    clearForm();
                } else {
                    showErrorSnackbar("Failed to add bus");
                }
            });
        }
    }

    private void showSuccessSnackbar(String message) {
        Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG).show();
    }

    private void clearForm() {
        startFormEditText.setText("");
        endDestinationEditText.setText("");
        busRouteNoEditText.setText("");
        busNumberEditText.setText("");
        departureTimeEditText.setText("");
        arrivalTimeEditText.setText("");
        seatCountEditText.setText("");
        busTypeSpinner.setSelection(0);
        runningConditionSpinner.setSelection(0);
    }
}
