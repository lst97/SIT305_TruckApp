package com.example.truckapp.models.truck;

import com.example.truckapp.models.IModel;

public class Truck implements IModel {
    private int id;
    private String name;
    private String description;
    private String title;
    private String image;
    private double price;
    private int type;
    private String location;
    private double latitude;
    private double longitude;

    public  Truck(int id, String name, String description, String title, String image, double price, int type, String location, double latitude, double longitude) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.title = title;
        this.image = image;
        this.price = price;
        this.type = type;
        this.location = location;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Truck() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }



}
