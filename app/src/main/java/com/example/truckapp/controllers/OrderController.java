package com.example.truckapp.controllers;

import com.example.truckapp.models.order.Order;
import com.example.truckapp.persistence.DbContext;

import java.util.List;

public class OrderController implements IController{
    // singleton
    private static OrderController instance = null;
    private final ServicesController servicesController;

    private OrderController() {
        servicesController = ServicesController.getInstance();
    }

    public static OrderController getInstance() {
        if (instance == null) {
            instance = new OrderController();
        }
        return instance;
    }
    @Override
    public boolean create(Object order) {
        DbContext dbContext = (DbContext) servicesController.getService("DatabaseService");
        return dbContext.createOrder((Order) order);
    }

    @Override
    public void update(Object object) {

    }

    @Override
    public void delete() {

    }

    @Override
    public List<Object> read() {
        return null;
    }
}
