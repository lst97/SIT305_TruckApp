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
import com.example.truckapp.adapters.HomeRecyclerViewAdapter;
import com.example.truckapp.controllers.TruckController;
import com.example.truckapp.models.truck.Truck;

import java.util.List;
import java.util.stream.Collectors;


// Home
public class HomeActivity extends AppCompatActivity {

    List<Truck> trucks;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        RecyclerView truckRecyclerView = findViewById(R.id.home_recycler_view);
        setupTruckModel();
        HomeRecyclerViewAdapter adapter = new HomeRecyclerViewAdapter(this, trucks);
        truckRecyclerView.setAdapter(adapter);
        truckRecyclerView.setLayoutManager(new LinearLayoutManager(this));
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
             // handle menu item 2
             return true;
         } else if (item.getItemId() == R.id.menu_main_items_my_orders) {
             // handle menu item 3
             return true;
         } else {
             return super.onOptionsItemSelected(item);
         }

    }

    private void setupTruckModel() {
        TruckController truckController = TruckController.getInstance();
        trucks = truckController.read().stream()
            .map(obj -> (Truck) obj) // cast the object to Truck
            .collect(Collectors.toList()); // collect the Truck to a list
    }
}