package com.example.truckapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.truckapp.R;
import com.example.truckapp.handlers.RepositoryHandler;
import com.example.truckapp.handlers.ServicesHandler;
import com.example.truckapp.models.order.GoodTypes;
import com.example.truckapp.models.order.Order;
import com.example.truckapp.models.truck.TruckTypes;
import com.example.truckapp.models.user.User;
import com.example.truckapp.persistence.OrderRepository;
import com.example.truckapp.services.cookie.CookieService;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class GoodTypeActivity extends AppCompatActivity {

    Spinner goodTypeSpinner;
    EditText goodTypeOtherEditText;
    EditText goodTypeWeightEditText;
    EditText goodTypeHeightEditText;
    EditText goodTypeWidthEditText;
    EditText goodTypeLengthEditText;
    Spinner vehicleTypeSpinner;
    EditText vehicleTypeOtherEditText;
    TextView goodTypeMessageTextView;
    Button createOrderButton;

    private void setupSpinnerAdapter() {

        // setup good type spinner
        Spinner goodTypeSpinner = findViewById(R.id.goodtype_type_spinner);
        String[] goodTypes = GoodTypes.GOOD_TYPES;
        ArrayAdapter<String> goodTypeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, goodTypes);
        goodTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        goodTypeSpinner.setAdapter(goodTypeAdapter);

        // setup vehicle type spinner
        Spinner vehicleTypeSpinner = findViewById(R.id.goodtype_vehicle_type_spinner);
        String[] vehicleTypes = TruckTypes.TRUCK_TYPES;
        ArrayAdapter<String> vehicleTypeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, vehicleTypes);
        vehicleTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        vehicleTypeSpinner.setAdapter(vehicleTypeAdapter);

    }

    private void initViews() {
        goodTypeSpinner = findViewById(R.id.goodtype_type_spinner);
        goodTypeOtherEditText = findViewById(R.id.goodtype_type_other_input);
        goodTypeWeightEditText = findViewById(R.id.goodtype_weight_input);
        goodTypeHeightEditText = findViewById(R.id.goodtype_height_input);
        goodTypeWidthEditText = findViewById(R.id.goodtype_width_input);
        goodTypeLengthEditText = findViewById(R.id.goodtype_length_input);
        vehicleTypeSpinner = findViewById(R.id.goodtype_vehicle_type_spinner);
        vehicleTypeOtherEditText = findViewById(R.id.goodtype_vehicle_type_other_input);
        goodTypeMessageTextView = findViewById(R.id.goodtype_message);
        createOrderButton = findViewById(R.id.goodtype_create_order_btn);

    }

    private void initListeners() {
        // set on click listener for create order button
        createOrderButton.setOnClickListener(v -> {
            // check if input is empty
            if (goodTypeWeightEditText.getText().toString().isEmpty()) {
                goodTypeWeightEditText.setError("Weight is required");
                return;
            }

            if (goodTypeHeightEditText.getText().toString().isEmpty()) {
                goodTypeHeightEditText.setError("Height is required");
                return;
            }

            if (goodTypeWidthEditText.getText().toString().isEmpty()) {
                goodTypeWidthEditText.setError("Width is required");
                return;
            }

            if (goodTypeLengthEditText.getText().toString().isEmpty()) {
                goodTypeLengthEditText.setError("Length is required");
                return;
            }

            if (goodTypeSpinner.getSelectedItem().toString().equals("Other") && goodTypeOtherEditText.getText().toString().isEmpty()) {
                goodTypeOtherEditText.setError("Other type is required");
                return;
            }

            if (vehicleTypeSpinner.getSelectedItem().toString().equals("Other") && vehicleTypeOtherEditText.getText().toString().isEmpty()) {
                vehicleTypeOtherEditText.setError("Other type is required");
                return;
            }

            // get user input
            String goodType = goodTypeSpinner.getSelectedItem().toString();
            if (goodType.equals("Other")) {
                goodType = goodTypeOtherEditText.getText().toString();
            }

            String vehicleType = vehicleTypeSpinner.getSelectedItem().toString();
            if (vehicleType.equals("Other")) {
                // That made no use for the truck_types table
                vehicleType = vehicleTypeOtherEditText.getText().toString();
            }

            // Get the intent that started the activity
            Intent intent = getIntent();

            // Retrieve the data from the intent
            String receiverName = intent.getStringExtra("receiverName");
            String pickupDate = intent.getStringExtra("date");
            String pickupTime = intent.getStringExtra("time");
            String pickupLocation = intent.getStringExtra("location");
            String truckId = intent.getStringExtra("truckId");

            String weight = goodTypeWeightEditText.getText().toString();
            String height = goodTypeHeightEditText.getText().toString();
            String width = goodTypeWidthEditText.getText().toString();
            String length = goodTypeLengthEditText.getText().toString();

            // get user id from CookieServices
            CookieService cookieService = (CookieService) ServicesHandler.getInstance().getService("CookieService");
            User user = cookieService.getUserSession();

            OrderRepository orderRepository = (OrderRepository) RepositoryHandler.getInstance().getRepository("OrderRepository");
            boolean isSucceed = orderRepository.create(new Order(
                    user.getId(),
                    Integer.parseInt(truckId),
                    receiverName,
                    LocalDate.parse(pickupDate, DateTimeFormatter.ofPattern("yyyy-M-d")),
                    LocalTime.parse(pickupTime),
                    pickupLocation, goodType,
                    Double.valueOf(weight),
                    Double.valueOf(width),
                    Double.valueOf(length),
                    Double.valueOf(height),
                    vehicleType)
            );

            if (isSucceed) {
                Intent intent1 = new Intent(GoodTypeActivity.this, HomeActivity.class);
                startActivity(intent1);
            } else {
                goodTypeMessageTextView.setText("Something went wrong, please try again");
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_good_type);
        initViews();
        setupSpinnerAdapter();
        initListeners();
    }
}
