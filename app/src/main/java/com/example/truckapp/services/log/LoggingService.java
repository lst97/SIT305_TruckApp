package com.example.truckapp.services.log;

import com.example.truckapp.services.ServiceFactory;

public class LoggingService implements ServiceFactory {
    private String serviceName;

    public LoggingService() {
        onCreate();
    }

    private void onCreate() {

    }

    @Override
    public String getServiceName() {
        return serviceName;
    }

    @Override
    public void setServiceName(String name) {
        this.serviceName = name;
    }

    @Override
    public void setLogService(LoggingService loggingService) {
        // not used here
    }

    public void log(String message, int type) {
        String TAG = "Logging Service";
        switch (type) {
            case 0:
                android.util.Log.i(TAG, serviceName + ": " + message);
                break;
            case 1:
                android.util.Log.w(TAG, serviceName + ": " + message);
                break;
            case 2:
                android.util.Log.d(TAG, serviceName + ": " + message);
                break;
            default:
                android.util.Log.e(TAG, serviceName + ": " + message);
                break;
        }
    }
}
