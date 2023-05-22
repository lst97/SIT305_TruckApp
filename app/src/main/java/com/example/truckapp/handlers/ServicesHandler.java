package com.example.truckapp.handlers;

import com.example.truckapp.services.ServiceFactory;
import com.example.truckapp.services.log.LoggingService;

import java.util.ArrayList;
import java.util.List;

// apply Single Responsibility Principle (SRP) for ServicesController
public class ServicesHandler implements ServicesHandlerFactory {
    private static ServicesHandler instance;
    private final List<ServiceFactory> servicesList = new ArrayList<>();
    private LoggingService loggingService = null;

    private ServicesHandler() {
        onCreate();
    }

    // Singleton pattern
    public static ServicesHandler getInstance() {
        if (instance == null) {
            instance = new ServicesHandler();
        }
        return instance;
    }

    private void onCreate() {
        loggingService = new LoggingService();
        this.addService("LoggingService", loggingService, false);
    }

    public void addService(String serviceName, ServiceFactory service, boolean isLogEnable) {
        ServiceFactory existService = this.getService(serviceName);
        if (existService == null) {
            service.setServiceName(serviceName);
            if (isLogEnable) {
                service.setLogService(loggingService);
            }
            servicesList.add(service);
        }
    }

    public ServiceFactory getService(String name) {
        for (ServiceFactory service : servicesList) {
            if (service.getServiceName().equals(name)) {
                return service;
            }
        }
        return null;
    }
}
