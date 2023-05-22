package com.example.truckapp.models.order;

import java.time.LocalDate;
import java.time.LocalTime;

public class Order{

    private int id;
    private final int userId;
    private final int vehicleId;
    private final String receiverName;
    private final LocalDate pickupDate;
    private final LocalTime pickupTime;
    private final String pickupLocation;
    private final String goodType;
    private final Double weight;
    private final Double width;
    private final Double length;
    private final Double height;
    private final String vehicleType;

    public Order(int id, int userId, int vehicleId, String receiverName, LocalDate pickupDate, LocalTime pickupTime,
                 String pickupLocation, String goodType, Double weight, Double width, Double length,
                 Double height, String vehicleType) {
        this.id = id;
        this.userId = userId;
        this.vehicleId = vehicleId;
        this.receiverName = receiverName;
        this.pickupDate = pickupDate;
        this.pickupTime = pickupTime;
        this.pickupLocation = pickupLocation;
        this.goodType = goodType;
        this.weight = weight;
        this.width = width;
        this.length = length;
        this.height = height;
        this.vehicleType = vehicleType;
    }

    // for create order
    public Order(int userId, int vehicleId, String receiverName, LocalDate pickupDate, LocalTime pickupTime,
                 String pickupLocation, String goodType, Double weight, Double width, Double length,
                 Double height, String vehicleType) {
        this.userId = userId;
        this.vehicleId = vehicleId;
        this.receiverName = receiverName;
        this.pickupDate = pickupDate;
        this.pickupTime = pickupTime;
        this.pickupLocation = pickupLocation;
        this.goodType = goodType;
        this.weight = weight;
        this.width = width;
        this.length = length;
        this.height = height;
        this.vehicleType = vehicleType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public int getVehicleId() {
        return vehicleId;
    }


    public String getReceiverName() {
        return receiverName;
    }

    public LocalDate getPickupDate() {
        return pickupDate;
    }


    public LocalTime getPickupTime() {
        return pickupTime;
    }



    public String getPickupLocation() {
        return pickupLocation;
    }


    public String getGoodType() {
        return goodType;
    }

    public Double getWeight() {
        return weight;
    }

    public Double getWidth() {
        return width;
    }

    public Double getLength() {
        return length;
    }

    public Double getHeight() {
        return height;
    }

    public String getVehicleType() {
        return vehicleType;
    }
}
