package com.example.truckapp.services.log;

import com.example.truckapp.controllers.ServicesController;
import com.example.truckapp.services.IServices;

public class LoggingService implements ILoggingService, IServices {
    private static String componentName;
    public boolean status = false;

    public LoggingService(String name, ServicesController servicesCtrl, boolean useLoggingService) {
        // servicesController is not used in this service
        // useLoggingService is not used in this service
        componentName = name;
        start();
    }

    @Override
    public int start() {
        status = true;
        return 0;
    }

    @Override
    public int stop() {
        status = false;
        return 0;
    }

    @Override
    public String getServiceName() {
        String serviceName = "LoggingService";
        return serviceName;
    }

    public void log(String message, int type) {
        if (!status) return;

        String TAG = "Logging Service";
        switch (type) {
            case 0:
                android.util.Log.i(TAG, componentName + ": " + message);
                break;
            case 1:
                android.util.Log.w(TAG, componentName + ": " + message);
                break;
            case 2:
                android.util.Log.d(TAG, componentName + ": " + message);
                break;
            default:
                android.util.Log.e(TAG, componentName + ": " + message);
                break;
        }
    }
}
