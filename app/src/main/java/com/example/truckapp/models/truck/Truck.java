package com.example.truckapp.models.truck;


public class Truck {
    private int id;
    private String name;
    private final String description;
    private final String title;
    private String image;
    private final double price;
    private final int type;
    private final String location;
    private final double latitude;
    private final double longitude;

    public Truck(int id, String name, String description, String title, String image, double price, int type, String location, double latitude, double longitude) {
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


    public String getTitle() {
        return title;
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


    public int getType() {
        return type;
    }


    public String getLocation() {
        return location;
    }


    public double getLatitude() {
        return latitude;
    }



    public double getLongitude() {
        return longitude;
    }




}
