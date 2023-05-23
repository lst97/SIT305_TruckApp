package com.example.truckapp.utils;

public class FeeCalculatorUtil {
    public static double calculateFee(double baseRatePerMinute, double additionalRatePerMinute, String estimatedTime) {
        int estimatedMinutes = convertToMinutes(estimatedTime);

        return baseRatePerMinute + (additionalRatePerMinute * (estimatedMinutes - 1));
    }

    private static int convertToMinutes(String estimatedTime) {
        // Parse the estimated time string and extract the minutes
        // Example: "1 hour 30 mins" -> 90 minutes

        String[] parts = estimatedTime.split(" ");

        int hours = 0;
        int minutes = 0;

        if(parts.length == 4){
            hours = Integer.parseInt(parts[0]);
            minutes = Integer.parseInt(parts[2]);
        } else if(parts.length == 2){
            minutes = Integer.parseInt(parts[0]);
        }

        return (hours * 60) + minutes;
    }
}