package com.example.truckapp.helpers;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class DirectionsApiHelper {
    private static StringBuilder response;
    private static void makeRequest(String apiKey, LatLng source, LatLng destination) throws IOException, JSONException{
        String apiUrl = "https://maps.googleapis.com/maps/api/directions/json?" +
                "origin=" + source.latitude + "," + source.longitude +
                "&destination=" + destination.latitude + "," + destination.longitude +
                "&key=" + apiKey;

        URL url = new URL(apiUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("GET");

        response = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
        }

        connection.disconnect();
    }
    public static List<LatLng> getRouteCoordinates(String apiKey, LatLng source, LatLng destination) throws IOException, JSONException {
        if (response == null) {
            makeRequest(apiKey, source, destination);
        }

        List<LatLng> routeCoordinates = new ArrayList<>();

        // Parse the JSON response
        JSONObject jsonResponse = new JSONObject(response.toString());
        JSONArray routesArray = jsonResponse.getJSONArray("routes");
        if (routesArray.length() > 0) {
            JSONObject routeObject = routesArray.getJSONObject(0);
            JSONObject overviewPolylineObject = routeObject.getJSONObject("overview_polyline");
            String encodedPolyline = overviewPolylineObject.getString("points");

            // Decode the polyline into coordinates
            routeCoordinates = decodePolyline(encodedPolyline);

            JSONArray legsArray = routeObject.getJSONArray("legs");
            if (legsArray.length() > 0) {
                JSONObject legObject = legsArray.getJSONObject(0);
                JSONObject durationObject = legObject.getJSONObject("duration");
            }
        }

        return routeCoordinates;
    }

    public static String getEstimateTime(String apiKey, LatLng source, LatLng destination) throws IOException, JSONException {
        if (response == null) {
            makeRequest(apiKey, source, destination);
        }

        // Parse the JSON response
        JSONObject jsonResponse = new JSONObject(response.toString());
        JSONArray routesArray = jsonResponse.getJSONArray("routes");

        String estimateTime = null;
        if (routesArray.length() > 0) {
            JSONObject routeObject = routesArray.getJSONObject(0);
            JSONArray legsArray = routeObject.getJSONArray("legs");
            if (legsArray.length() > 0) {
                JSONObject legObject = legsArray.getJSONObject(0);
                JSONObject durationObject = legObject.getJSONObject("duration");
                estimateTime = durationObject.getString("text");
            }
        }

        return estimateTime;
    }

    public static void onFinish() {
        response = null;
    }

    private static List<LatLng> decodePolyline(String encodedPolyline) {
        List<LatLng> polyline = new ArrayList<>();
        int index = 0;
        int latitude = 0;
        int longitude = 0;
        int len = encodedPolyline.length();

        while (index < len) {
            int shift = 0;
            int result = 0;
            int b;
            do {
                b = encodedPolyline.charAt(index++) - 63;
                result |= (b & 0x1F) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            latitude += dlat;

            shift = 0;
            result = 0;
            do {
                b = encodedPolyline.charAt(index++) - 63;
                result |= (b & 0x1F) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            longitude += dlng;

            LatLng point = new LatLng(latitude / 1E5, longitude / 1E5);
            polyline.add(point);
        }

        return polyline;
    }
}