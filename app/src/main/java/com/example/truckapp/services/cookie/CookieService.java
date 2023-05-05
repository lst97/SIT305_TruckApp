package com.example.truckapp.services.cookie;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.truckapp.controllers.ServicesController;
import com.example.truckapp.models.user.User;
import com.example.truckapp.services.log.LoggingService;
import com.google.gson.Gson;

import com.fasterxml.jackson.databind.ObjectMapper;

import kotlin.NotImplementedError;


public class CookieService implements ICookie {
    private final String serviceName;
    private ServicesController servicesController;
    private Context applicationContext;
    private  LoggingService loggingService = null;

    private static boolean status = false;
    public int start(){
        status = true;
        return 0;
    }
    public int stop(){
        status = false;
        return 0;
    }

    public void setContext(Context context){
        applicationContext = context;
    }

    public CookieService(String name, ServicesController servicesCtrl, boolean useLoggingService){
        servicesController = servicesCtrl;
        if (useLoggingService){
            loggingService = (LoggingService) servicesController.getService(name);
        }

        serviceName = name;
        start();
    }

    public User getUserSession(){
        SharedPreferences prefs = applicationContext.getSharedPreferences(CookieTypes.SESSION, Context.MODE_PRIVATE);

        // check if prefs contains user session
        if (!prefs.contains(CookieTypes.SESSION)){
            return null;
        }

        String jsonString = prefs.getString("data", "");
        // Create an ObjectMapper instance
        ObjectMapper mapper = new ObjectMapper();

        // Convert JSON string to User object
        User user = null;
        try {
            user = mapper.readValue(jsonString, User.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(user == null){
            return null;
        }

        user.setCreatedDate(null);
        user.setModifiedDate(null);
        return user;
    }

    @Override
    public void addSession(User user){

        SharedPreferences prefs = applicationContext.getSharedPreferences(CookieTypes.SESSION, MODE_PRIVATE);

        // check if the prefs name already exists
        if (prefs.contains("data")){
            // prefs name already exists
            // remove the prefs name
            applicationContext.getSharedPreferences(CookieTypes.SESSION, MODE_PRIVATE).edit().remove("data").apply();
        }

        // create a new editor for SharedPreferences
        SharedPreferences.Editor editor = prefs.edit();

        // put the object into SharedPreferences as a string
        Gson gson = new Gson();
        String json = gson.toJson(user);
        editor.putString("data", json);
        editor.apply();
    }

    @Override
    public void addCookie(String name) {
        // not yet used
        throw new NotImplementedError();
    }

    @Override
    public void removeCookie() {

    }

    @Override
    public SharedPreferences getCookie(String name) {
        throw new NotImplementedError();
    }

    @Override
    public String getServiceName() {
        return serviceName;
    }
}
