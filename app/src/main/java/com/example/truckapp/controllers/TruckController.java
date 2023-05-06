package com.example.truckapp.controllers;

import com.example.truckapp.models.truck.Truck;
import com.example.truckapp.persistence.DbContext;
import com.example.truckapp.services.authenticate.AccessToken;

import java.util.ArrayList;
import java.util.List;

public class TruckController implements IController {
    private static TruckController instance;
    private List<Truck> trucks;
    private final ServicesController servicesController;


    private TruckController() {
        servicesController = ServicesController.getInstance();
    }

    public static TruckController getInstance() {
        if (instance == null) {
            instance = new TruckController();
        }
        return instance;
    }

    @Override
    public boolean create(Object truck) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void update(Object truck) {
        DbContext dbContext = (DbContext) servicesController.getService("DatabaseService");
        dbContext.updateTruck((Truck) truck);
    }

    public void update(Object truck, AccessToken accessToken) {
        ((DbContext) servicesController.getService("DatabaseService")).setAccessToken(accessToken);
        this.update(truck);
    }

    @Override
    public void delete() {

    }

    public List<Truck> getAvailableTruck() {
        DbContext dbContext = (DbContext) servicesController.getService("DatabaseService");
        return dbContext.getAvailableTruck();
    }

    public Truck getTruckByName(String name) {
        DbContext dbContext = (DbContext) servicesController.getService("DatabaseService");
        return dbContext.getTruckByName(name);
    }

    @Override
    public List<Object> read() {
        DbContext dbContext = (DbContext) servicesController.getService("DatabaseService");
        trucks = dbContext.getTrucks();
        return new ArrayList<>(trucks);
    }

    public List<Object> read(AccessToken accessToken) {
        ((DbContext) servicesController.getService("DatabaseService")).setAccessToken(accessToken);
        return this.read();
    }
}
