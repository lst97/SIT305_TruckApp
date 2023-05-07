package com.example.truckapp.models.order;

import com.example.truckapp.models.IModel;

import java.time.LocalDate;
import java.time.LocalTime;

public class Order implements IModel {

    private int id;
    private int userId;
    private int vehicleId;
    private String receiverName;
    private LocalDate pickupDate;
    private LocalTime pickupTime;
    private String pickupLocation;
    private String goodType;
    private Double weight;
    private Double width;
    private Double length;
    private Double height;
    private String vehicleType;

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

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(int vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public LocalDate getPickupDate() {
        return pickupDate;
    }

    public void setPickupDate(LocalDate pickupDate) {
        this.pickupDate = pickupDate;
    }

    public LocalTime getPickupTime() {
        return pickupTime;
    }

    public void setPickupTime(LocalTime pickupTime) {
        this.pickupTime = pickupTime;
    }

    public String getPickupLocation() {
        return pickupLocation;
    }

    public void setPickupLocation(String pickupLocation) {
        this.pickupLocation = pickupLocation;
    }

    public String getGoodType() {
        return goodType;
    }

    public void setGoodType(String goodType) {
        this.goodType = goodType;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public Double getWidth() {
        return width;
    }

    public void setWidth(Double width) {
        this.width = width;
    }

    public Double getLength() {
        return length;
    }

    public void setLength(Double length) {
        this.length = length;
    }

    public Double getHeight() {
        return height;
    }

    public void setHeight(Double height) {
        this.height = height;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    // getters and setters omitted for brevity

}
