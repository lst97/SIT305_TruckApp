package com.example.truckapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.truckapp.R;
import com.example.truckapp.adapters.TruckRecyclerViewAdapter;
import com.example.truckapp.controllers.OrderController;
import com.example.truckapp.controllers.ServicesController;
import com.example.truckapp.controllers.TruckController;
import com.example.truckapp.models.order.Order;
import com.example.truckapp.models.truck.Truck;
import com.example.truckapp.models.user.User;
import com.example.truckapp.services.cookie.CookieService;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class OrderActivity extends AppCompatActivity {
    List<Order> orders;
    List<Truck> trucks;

    FloatingActionButton addDeliveryBtn;

    private void setupTruckModel() {
        OrderController orderController = OrderController.getInstance();

        CookieService cookieService = (CookieService) ServicesController.getInstance().getService("CookieService");
        User user = cookieService.getUserSession();
        orders = orderController.getOrders(user);

        trucks = new ArrayList<>();
        TruckController truckController = TruckController.getInstance();
        for (Order order : orders) {
            trucks.add(truckController.getTruckById(order.getVehicleId()));
        }
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
            Intent intent = new Intent(OrderActivity.this, HomeActivity.class);
            startActivity(intent);
            return true;
        } else if (item.getItemId() == R.id.menu_main_items_account) {
            // NO REQUIRED FROM TASK
            return true;
        } else if (item.getItemId() == R.id.menu_main_items_my_orders) {
            Intent intent = new Intent(OrderActivity.this, OrderActivity.class);
            startActivity(intent);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        addDeliveryBtn = findViewById(R.id.order_floating_new_order);
        RecyclerView truckRecyclerView = findViewById(R.id.order_recycler_view);

        setupTruckModel();

        TruckRecyclerViewAdapter adapter = new TruckRecyclerViewAdapter(this, trucks);
        truckRecyclerView.setAdapter(adapter);
        truckRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        addDeliveryBtn.setOnClickListener(v -> {
            Intent intent = new Intent(OrderActivity.this, NewDeliveryActivity.class);
            startActivity(intent);
        });
    }

}
