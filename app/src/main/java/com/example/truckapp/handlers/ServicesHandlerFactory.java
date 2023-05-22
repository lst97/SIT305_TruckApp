package com.example.truckapp.handlers;

import com.example.truckapp.services.ServiceFactory;

public interface ServicesHandlerFactory {
    ServiceFactory getService(String serviceName);

    void addService(String serviceName, ServiceFactory service, boolean isLogEnable);
}
