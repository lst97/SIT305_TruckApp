package com.example.truckapp;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Menu;
import android.view.MenuInflater;

import androidx.core.content.ContextCompat;

import com.example.truckapp.activities.LoginActivity;
import com.example.truckapp.controllers.ServicesController;
import com.example.truckapp.controllers.TruckController;
import com.example.truckapp.models.truck.Truck;
import com.example.truckapp.persistence.DbContext;
import com.example.truckapp.services.authenticate.AccessToken;
import com.example.truckapp.services.authenticate.AuthenticateService;
import com.example.truckapp.services.cookie.CookieService;
import com.example.truckapp.services.log.LoggingService;
import com.example.truckapp.utils.ImageUtil;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class TruckApp extends Activity {
    private void initializeServices() {
        // service will be start automatically
        ServicesController servicesController = ServicesController.getInstance();
        servicesController.addService("LoggingService", LoggingService.class.getName(), false);

        servicesController.addService("CookieService", CookieService.class.getName(), true);
        ((CookieService) Objects.requireNonNull(servicesController.getService("CookieService"))).setContext(this);

        servicesController.addService("DatabaseService", DbContext.class.getName(), true);
        servicesController.addService("AuthenticateService", AuthenticateService.class.getName(), true);
    }

    private void updateTrucksImage(){

        TruckController truckController = TruckController.getInstance();

        // Database is not allowed to be use if the user is not logged in
        // unless the access token is set
        List<Truck> trucks = truckController.read(new AccessToken()).stream().map(truck -> (Truck) truck).collect(Collectors.toList());

        List<Drawable> drawables = new ArrayList<>();
        drawables.add(ContextCompat.getDrawable(this, R.drawable.nelson_realistic_mini_truck_natural_light_f348aa80_d0b5_4f64_949c_6fc3fe7629d7));
        drawables.add(ContextCompat.getDrawable(this, R.drawable.nelson_realistic_mini_truck_natural_light_75ba2e9e_6c5d_4124_8d7b_8f6327555be1));
        drawables.add(ContextCompat.getDrawable(this, R.drawable.nelson_realistic_mini_truck_natural_light_09966598_d806_4e1e_9539_92b48ae4203a));
        drawables.add(ContextCompat.getDrawable(this, R.drawable.nelson_realistic_mini_truck_natural_light_e58ba802_b445_4089_8cb2_c6ee31411fc3));
        drawables.add(ContextCompat.getDrawable(this, R.drawable.nelson_realistic_refrigerate_truck_natural_light_03b5e7a0_0198_49b9_b30a_dbf9521b2799));
        drawables.add(ContextCompat.getDrawable(this, R.drawable.nelson_realistic_refrigerate_truck_natural_light_6feb17cd_c1cf_40a9_9740_b69a427771db));
        drawables.add(ContextCompat.getDrawable(this, R.drawable.nelson_realistic_truck_natural_light_540e5a68_209e_478a_af0b_66ec02a59210));
        drawables.add(ContextCompat.getDrawable(this, R.drawable.nelson_realistic_truck_natural_light_31b63313_a545_4326_a323_a57aa0c02851));
        drawables.add(ContextCompat.getDrawable(this, R.drawable.nelson_realistic_van_natural_light_61c92aa7_32ab_41cd_8c64_f60c3779265a));
        drawables.add(ContextCompat.getDrawable(this, R.drawable.nelson_realistic_van_natural_light_city_road_ea321f5f_a00b_4c9c_89a3_61365f8b85ed));

        // only for 10 mock trucks data
        for(int i = 0; i < trucks.size(); i++){

            Drawable drawable = drawables.get(i);
            Bitmap bitmap = ((BitmapDrawable)drawable).getBitmap();
            trucks.get(i).setImage(ImageUtil.encodeImage(bitmap));

            truckController.update(trucks.get(i), new AccessToken());
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        initializeServices();

        // clear user session (the app do not support remember me function)
        CookieService cookieService = (CookieService) ServicesController.getInstance().getService("CookieService");
        cookieService.removeUserSession();

        // this should the backend job, but since I did not create the backend
        // I have to do it here for initialize the DB
        updateTrucksImage(); // update the trucks image from URL to BASE64 String

        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}
