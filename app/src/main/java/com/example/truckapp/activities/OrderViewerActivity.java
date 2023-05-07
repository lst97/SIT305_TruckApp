package com.example.truckapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.truckapp.R;
import com.example.truckapp.controllers.OrderController;
import com.example.truckapp.controllers.TruckController;
import com.example.truckapp.models.order.Order;
import com.example.truckapp.models.truck.Truck;
import com.example.truckapp.utils.ImageUtil;

public class OrderViewerActivity extends AppCompatActivity {
    ImageView truckImage;
    TextView fromSenderTextView;
    TextView toReceiverTextView;
    TextView pickupTimeTextView;
    TextView dropoffTimeTextView;
    TextView weightTextView;
    TextView typeTextView;
    TextView widthTextView;
    TextView heightTextView;
    TextView lengthTextView;
    Button callButton;

    Order order;
    Truck truck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_viewer);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        fromSenderTextView = findViewById(R.id.order_from_sender_label);
        toReceiverTextView = findViewById(R.id.order_to_receiver_label);
        pickupTimeTextView = findViewById(R.id.order_pickup_time_label);
        dropoffTimeTextView = findViewById(R.id.order_dropoff_time_label);
        weightTextView = findViewById(R.id.order_weight_label);
        typeTextView = findViewById(R.id.order_type_label);
        widthTextView = findViewById(R.id.order_width_label);
        heightTextView = findViewById(R.id.order_height_label);
        lengthTextView = findViewById(R.id.order_length_label);
        callButton = findViewById(R.id.order_call_btn);
        truckImage = findViewById(R.id.order_truck_image);

        // get truckId from intent
        Intent intent = getIntent();
        int truckId = intent.getIntExtra("truckId", -1);

        // get truck image
        TruckController truckController = TruckController.getInstance();
        truck = truckController.getTruckById(truckId);
        truckImage.setImageBitmap(ImageUtil.decodeImage(truck.getImage()));

        // get orders from truckId
        OrderController orderController = OrderController.getInstance();
        order = orderController.getOrderByTruckId(truckId);

        fromSenderTextView.setText(getString(R.string.order_from_sender, truck.getName()));
        toReceiverTextView.setText(getString(R.string.order_to_receiver, order.getReceiverName()));
        pickupTimeTextView.setText(getString(R.string.order_pickup_time, order.getPickupTime().toString()));
        dropoffTimeTextView.setText(getString(R.string.order_dropoff_time));
        weightTextView.setText(getString(R.string.order_weight, order.getWeight().toString()));
        typeTextView.setText(getString(R.string.order_type, order.getGoodType()));
        widthTextView.setText(getString(R.string.order_width, order.getWidth().toString()));
        heightTextView.setText(getString(R.string.order_height, order.getHeight().toString()));
        lengthTextView.setText(getString(R.string.order_length, order.getLength().toString()));

        // set call button onclick listener
        callButton.setOnClickListener(v -> {
            Intent callIntent = new Intent(Intent.ACTION_DIAL);
            callIntent.setData(android.net.Uri.parse("tel:" + "123456789"));
            startActivity(callIntent);
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main_items, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_main_items_home) {
            // go to home
            Intent intent = new Intent(OrderViewerActivity.this, HomeActivity.class);
            startActivity(intent);
            return true;
        } else if (item.getItemId() == R.id.menu_main_items_account) {
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(this, "NOT_IMPLEMENTED", duration);
            toast.show();
            return true;
        } else if (item.getItemId() == R.id.menu_main_items_my_orders) {
            Intent intent = new Intent(OrderViewerActivity.this, OrderActivity.class);
            startActivity(intent);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
}
