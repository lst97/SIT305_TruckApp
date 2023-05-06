package com.example.truckapp.utils;

public class DataValidatorUtil {
    public static boolean checkTime(String time) {
        // Regular expression to match hh:mm:ss format
        return time.matches("([01]?[0-9]|2[0-3]):[0-5][0-9]");
    }
}
