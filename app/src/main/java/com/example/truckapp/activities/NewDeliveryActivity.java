package com.example.truckapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;

import com.example.truckapp.R;
import com.example.truckapp.controllers.TruckController;
import com.example.truckapp.models.truck.Truck;
import com.example.truckapp.utils.DataValidatorUtil;

import java.util.List;
import java.util.stream.Collectors;

public class NewDeliveryActivity extends AppCompatActivity {
    private EditText receiverNameEditText;
    private DatePicker datePicker;
    private EditText timeEditText;
    private EditText locationEditText;
    private TextView messageTextView;
    private Spinner truckSpinner;
    private Button nextButton;

    private List<Truck> trucks;

    private void setupSpinnerAdapter() {
        // get vehicle from database
        TruckController truckController = TruckController.getInstance();
        trucks = truckController.read().stream().map(obj -> (Truck) obj).collect(Collectors.toList());
        List<String> truckNames = trucks.stream().map(Truck::getName).collect(Collectors.toList());

        // set spinner items
        // Create an ArrayAdapter to bind the items to the spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, truckNames);
        // Set the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Set the adapter to the spinner
        truckSpinner.setAdapter(adapter);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        receiverNameEditText = findViewById(R.id.delivery_reciver_name_input);
        datePicker = findViewById(R.id.delivery_date_picker);
        timeEditText = findViewById(R.id.delivery_time_input);
        locationEditText = findViewById(R.id.delivery_location_input);
        nextButton = findViewById(R.id.delivery_next_btn);
        messageTextView = findViewById(R.id.delivery_message);
        truckSpinner = findViewById(R.id.delivery_select_vehicle_spinner);

        setupSpinnerAdapter();

        // set on click listener for next button
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // check if input is empty
                if (receiverNameEditText.getText().toString().isEmpty()) {
                    receiverNameEditText.setError("Receiver name is required");
                    return;
                }

                if (timeEditText.getText().toString().isEmpty()) {
                    timeEditText.setError("Time is required");
                    return;
                }

                // check time format using regex
                if (!DataValidatorUtil.checkTime(timeEditText.getText().toString())) {
                    timeEditText.setError("Time format is invalid");
                    return;
                }

                if (locationEditText.getText().toString().isEmpty()) {
                    locationEditText.setError("Location is required");
                    return;
                }

                if (datePicker.getDayOfMonth() == 0 || datePicker.getMonth() == 0 || datePicker.getYear() == 0) {
                    messageTextView.setText("Please pick a date");
                    return;
                }

                if (truckSpinner.getSelectedItem().toString().isEmpty()) {
                    messageTextView.setText("Please pick a truck");
                    return;
                }


                next(v);
            }
        });
    }

    public void next(View view) {
        String receiverName = receiverNameEditText.getText().toString();
        //  ISO-8601
        String date = datePicker.getYear() + "-" + datePicker.getMonth() + "-" + datePicker.getDayOfMonth();
        String time = timeEditText.getText().toString();
        String location = locationEditText.getText().toString();
        String truckId = String.valueOf(trucks.get(truckSpinner.getSelectedItemPosition()).getId());

        Intent intent = new Intent(this, GoodTypeActivity.class);
        intent.putExtra("receiverName", receiverName);
        intent.putExtra("date", date);
        intent.putExtra("time", time);
        intent.putExtra("location", location);
        intent.putExtra("truckId", truckId);
        startActivity(intent);
    }
}
