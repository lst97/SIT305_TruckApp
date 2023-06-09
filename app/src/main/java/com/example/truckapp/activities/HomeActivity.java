package com.example.truckapp.activities;

import android.content.Intent;
import android.os.Bundle;
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
import com.example.truckapp.models.truck.Truck;
import com.example.truckapp.persistence.TruckRepository;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;


// Home
public class HomeActivity extends AppCompatActivity {

    List<Truck> trucks;

    FloatingActionButton addDeliveryBtn;
    RecyclerView truckRecyclerView;

    private void initViews() {
        truckRecyclerView = findViewById(R.id.home_recycler_view);
        addDeliveryBtn = findViewById(R.id.home_floating_new_order);
    }

    private void initListeners() {
        addDeliveryBtn.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, NewDeliveryActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initViews();
        setupTruckModel();
        TruckRecyclerViewAdapter adapter = new TruckRecyclerViewAdapter(this, trucks);
        truckRecyclerView.setAdapter(adapter);
        truckRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        initListeners();
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
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(this, "NOT_IMPLEMENTED", duration);
            toast.show();
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
        TruckRepository truckRepository = (TruckRepository) RepositoryHandler.getInstance().getRepository("TruckRepository");
        trucks = truckRepository.getAvailableTrucks();
    }
}