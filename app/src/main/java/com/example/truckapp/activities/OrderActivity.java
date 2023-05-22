package com.example.truckapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.truckapp.R;
import com.example.truckapp.adapters.TruckRecyclerViewAdapter;
import com.example.truckapp.handlers.RepositoryHandler;
import com.example.truckapp.handlers.ServicesHandler;
import com.example.truckapp.models.order.Order;
import com.example.truckapp.models.truck.Truck;
import com.example.truckapp.models.user.User;
import com.example.truckapp.persistence.OrderRepository;
import com.example.truckapp.persistence.TruckRepository;
import com.example.truckapp.services.cookie.CookieService;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class OrderActivity extends AppCompatActivity {
    List<Order> orders;
    List<Truck> trucks;

    FloatingActionButton addDeliveryBtn;
    RecyclerView truckRecyclerView;

    private void setupTruckModel() {
        OrderRepository orderRepository = (OrderRepository) RepositoryHandler.getInstance().getRepository("OrderRepository");
        CookieService cookieService = (CookieService) ServicesHandler.getInstance().getService("CookieService");
        User user = cookieService.getUserSession();
        orders = orderRepository.getOrdersByUserId(user.getId());

        trucks = new ArrayList<>();
        TruckRepository truckRepository = (TruckRepository) RepositoryHandler.getInstance().getRepository("TruckRepository");
        for (Order order : orders) {
            trucks.add(truckRepository.read(order.getVehicleId()));
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
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(this, "NOT_IMPLEMENTED", duration);
            toast.show();
            return true;
        } else if (item.getItemId() == R.id.menu_main_items_my_orders) {
            Intent intent = new Intent(OrderActivity.this, OrderActivity.class);
            startActivity(intent);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void initViews(){
        addDeliveryBtn = findViewById(R.id.order_floating_new_order);
        truckRecyclerView = findViewById(R.id.order_recycler_view);
    }

    private void setupAdapter(){
        TruckRecyclerViewAdapter adapter = new TruckRecyclerViewAdapter(this, trucks);
        truckRecyclerView.setAdapter(adapter);
        truckRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void initListeners(){
        addDeliveryBtn.setOnClickListener(v -> {
            Intent intent = new Intent(OrderActivity.this, NewDeliveryActivity.class);
            startActivity(intent);
        });
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        initViews();
        setupTruckModel();
        setupAdapter();
        initListeners();
    }
}
