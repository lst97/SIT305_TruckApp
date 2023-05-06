package com.example.truckapp.models.truck;

public class TruckTypes {
    static int VAN = 1;
    static int TRUCK = 2;
    static int REFRIGERATE = 3;
    static int MINI_TRUCK = 4;
    static int OTHER = 5;

    public static String[] TRUCK_TYPES = {"VAN", "TRUCK", "REFRIGERATE", "MINI_TRUCK", "OTHER"};

    public static int getTruckTypeId(String type) {
        switch (type) {
            case "VAN":
                return VAN;
            case "TRUCK":
                return TRUCK;
            case "REFRIGERATE":
                return REFRIGERATE;
            case "MINI_TRUCK":
                return MINI_TRUCK;
            default:
                return OTHER;
        }
    }

    public static String getTruckTypeName(int id) {
        switch (id) {
            case 1:
                return "VAN";
            case 2:
                return "TRUCK";
            case 3:
                return "REFRIGERATE";
            case 4:
                return "MINI_TRUCK";
            default:
                return "OTHER";
        }
    }
}
