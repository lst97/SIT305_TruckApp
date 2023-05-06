package com.example.truckapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.truckapp.R;
import com.example.truckapp.adapters.TruckRecyclerViewAdapter;
import com.example.truckapp.controllers.TruckController;
import com.example.truckapp.models.truck.Truck;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;


// Home
public class HomeActivity extends AppCompatActivity {

    List<Truck> trucks;

    FloatingActionButton addDeliveryBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        RecyclerView truckRecyclerView = findViewById(R.id.home_recycler_view);
        setupTruckModel();
        TruckRecyclerViewAdapter adapter = new TruckRecyclerViewAdapter(this, trucks);
        truckRecyclerView.setAdapter(adapter);
        truckRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        addDeliveryBtn = findViewById(R.id.home_floating_new_order);
        addDeliveryBtn.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, NewDeliveryActivity.class);
            startActivity(intent);
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
             Intent intent = new Intent(HomeActivity.this, HomeActivity.class);
             startActivity(intent);
             return true;
         } else if (item.getItemId() == R.id.menu_main_items_account) {
             // NO REQUIRED FROM TASK
             return true;
         } else if (item.getItemId() == R.id.menu_main_items_my_orders) {
             Intent intent = new Intent(HomeActivity.this, OrderActivity.class);
             startActivity(intent);
             return true;
         } else {
             return super.onOptionsItemSelected(item);
         }
    }

    private void setupTruckModel() {
        TruckController truckController = TruckController.getInstance();
        trucks = truckController.getAvailableTrucks();
    }
}