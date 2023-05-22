package com.example.truckapp.services;

import com.example.truckapp.services.log.LoggingService;

public interface ServiceFactory {
    String getServiceName();

    void setServiceName(String name);

    void setLogService(LoggingService loggingService);
}
