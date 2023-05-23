package com.example.truckapp.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.truckapp.R;
import com.example.truckapp.helpers.DirectionsApiHelper;
import com.example.truckapp.utils.ApiKeysUtil;
import com.example.truckapp.utils.FeeCalculatorUtil;
import com.example.truckapp.utils.PaymentsUtil;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.wallet.AutoResolveHelper;
import com.google.android.gms.wallet.PaymentData;
import com.google.android.gms.wallet.PaymentDataRequest;
import com.google.android.gms.wallet.PaymentsClient;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

public class EstimateActivity extends AppCompatActivity {

    private static final int LOAD_PAYMENT_DATA_REQUEST_CODE = 123;
    private AutocompleteSupportFragment asfFrom;
    private AutocompleteSupportFragment asfTo;
    private MapView mapView;
    private TextView approxTimeTextView;
    private TextView approxFeeTextView;
    private Button bookButton;
    private Button callButton;

    private String locationNameFrom;
    private LatLng locationLatLngFrom;
    private String locationNameTo;
    private LatLng locationLatLngTo;
    private String approxTime;
    private Double approxFee;
    private PaymentsClient paymentsClient;

    private void initViews(Bundle savedInstanceState){
        asfFrom = (AutocompleteSupportFragment) getSupportFragmentManager().findFragmentById(R.id.estimate_autocomplete_from);
        asfTo = (AutocompleteSupportFragment) getSupportFragmentManager().findFragmentById(R.id.estimate_autocomplete_to);
        mapView = findViewById(R.id.estimate_map_view);
        approxTimeTextView = findViewById(R.id.estimate_approx_time);
        approxFeeTextView = findViewById(R.id.estimate_approx_fee);
        bookButton = findViewById(R.id.estimate_book_btn);
        callButton = findViewById(R.id.estimate_call_btn);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        paymentsClient = PaymentsUtil.createPaymentsClient(this);
    }

    private void initListeners(){
        callButton.setOnClickListener(v -> {
            // dial the number
            // Check for CALL_PHONE permission
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);

                // hardcoded phone number, recommend to get from database
                callIntent.setData(Uri.parse("tel:" + "1234567890"));
                startActivity(callIntent);
            } else {
                // Request CALL_PHONE permission
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 1);
            }

        });

        bookButton.setOnClickListener(v -> {

            if (!isBothLocationsSelected()) {
                Toast.makeText(this, "Please select both locations", Toast.LENGTH_SHORT).show();
                return;
            }

            requestPayment(v);
        });
    }

    public void requestPayment(View view) {

        Optional<JSONObject> paymentDataRequestJson = Optional.ofNullable(PaymentsUtil.getPaymentDataRequest(Math.round(approxFee)));
        if (!paymentDataRequestJson.isPresent()) {
            return;
        }

        PaymentDataRequest request = PaymentDataRequest.fromJson(paymentDataRequestJson.get().toString());

        AutoResolveHelper.resolveTask(paymentsClient.loadPaymentData(request),this, LOAD_PAYMENT_DATA_REQUEST_CODE);

    }

    private boolean isBothLocationsSelected(){
        return locationLatLngFrom != null && locationLatLngTo != null;
    }

    private void drawRoutesOnMap(GoogleMap map, List<LatLng> routeCoordinates){
        if (routeCoordinates == null || routeCoordinates.isEmpty()) {
            return;
        }

        PolylineOptions polylineOptions = new PolylineOptions();
        polylineOptions.addAll(routeCoordinates);
        Polyline polyline = map.addPolyline(polylineOptions);
        polyline.setColor(Color.BLUE);
        polyline.setWidth(5);
    }
    private List<LatLng> getRouteCoordinates() throws IOException, JSONException {
        return DirectionsApiHelper.getRouteCoordinates(ApiKeysUtil.GOOGLE_API_KEY, locationLatLngFrom, locationLatLngTo);
    }
    private String getApproxTime() throws IOException, JSONException {
        return DirectionsApiHelper.getEstimateTime(ApiKeysUtil.GOOGLE_API_KEY, locationLatLngFrom, locationLatLngTo);
    }
    private void setGoogleMapLocations() {
        mapView.getMapAsync(googleMap -> {
            googleMap.clear();
            MarkerOptions markerOptions1 = new MarkerOptions().position(locationLatLngFrom).title("From");
            MarkerOptions markerOptions2 = new MarkerOptions().position(locationLatLngTo).title("To");

            googleMap.addMarker(markerOptions1);
            googleMap.addMarker(markerOptions2);


            // get route coordinates and approx time string
            List<LatLng> routeCoordinates;
            try {
                routeCoordinates = getRouteCoordinates();
                approxTime = getApproxTime();
            } catch (IOException | JSONException e) {
                Toast.makeText(this, "Error getting route", Toast.LENGTH_SHORT).show();
                approxFeeTextView.setText("Error");
                approxTimeTextView.setText("Error");
                throw new RuntimeException(e);
            }

            DirectionsApiHelper.onFinish();

            drawRoutesOnMap(googleMap, routeCoordinates);
            approxTimeTextView.setText("Approx. Time: " + approxTime);

            // baseRate and additionalRate are hardcoded, recommend to get from database
            approxFee = FeeCalculatorUtil.calculateFee(20, 3, approxTime);
            approxFeeTextView.setText(String.format(Locale.getDefault(), "Approx. Fee: $%.2f", approxFee));

            // melbourne
            LatLng initialLocation = new LatLng(-37.8136, 144.9631);
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(initialLocation, 10));
        });
    }

    private void doEstimate(){
        if (isBothLocationsSelected()) {
            approxFeeTextView.setText("Calculating...");
            approxTimeTextView.setText("Calculating...");
            setGoogleMapLocations();
        }
    }
    private void initPlaceClient(){
        Places.initialize(getApplicationContext(), ApiKeysUtil.GOOGLE_API_KEY);
        Places.createClient(this);

        assert asfFrom != null;
        assert asfTo != null;
        asfFrom.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG));
        asfTo.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG));

        asfFrom.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                // Handle selected place
                EstimateActivity.this.locationNameFrom = place.getName();
                EstimateActivity.this.locationLatLngFrom = place.getLatLng();

                doEstimate();

                Toast.makeText(EstimateActivity.this, "Place: " + place.getName() + ", " + locationLatLngFrom.toString(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(@NonNull Status status) {
            }
        });

        asfTo.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                EstimateActivity.this.locationNameTo = place.getName();
                EstimateActivity.this.locationLatLngTo = place.getLatLng();

                doEstimate();

                Toast.makeText(EstimateActivity.this, "Place: " + place.getName() + ", " + locationLatLngFrom.toString(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(@NonNull Status status) {
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estimate);

        initViews(savedInstanceState);
        initListeners();
        initPlaceClient();

        // Initialize a Google Pay API client for an environment suitable for testing.
        // It's recommended to create the PaymentsClient object inside of the onCreate method.
        paymentsClient = PaymentsUtil.createPaymentsClient(this);
    }

    // Handle the payment result in onActivityResult
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LOAD_PAYMENT_DATA_REQUEST_CODE) {
            switch (resultCode) {
                case RESULT_OK:
                    PaymentData paymentData = PaymentData.getFromIntent(data);
                    // Process the payment data here
                    // Extract the necessary information from the paymentData object
                    break;
                case RESULT_CANCELED:
                    // The user canceled the payment process
                    break;
                case AutoResolveHelper.RESULT_ERROR:
                    // Handle any errors that occurred during the payment process
                    Status apiException = AutoResolveHelper.getStatusFromIntent(data);
                    assert apiException != null;
                    int statusCode = apiException.getStatusCode();
                    // Handle the error accordingly
                    break;
            }
        }
    }
}
