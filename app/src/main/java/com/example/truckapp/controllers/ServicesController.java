package com.example.truckapp.controllers;

import com.example.truckapp.services.IServices;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

// apply Single Responsibility Principle (SRP) for ServicesController
public class ServicesController implements IServices, IController {
    private static final List<IServices> servicesList = new ArrayList<>();
    private static ServicesController instance;
    private final String serviceName;

    private ServicesController() {
        serviceName = ServicesController.class.getName();
    }

    // Singleton pattern
    public static ServicesController getInstance() {
        if (instance == null) {
            instance = new ServicesController();
        }
        return instance;
    }

    @Override
    public String getServiceName() {
        return serviceName;
    }

    public void addService(String name, String className, boolean enableLoggingService) {
        try {
            Class<?> serviceClass = Class.forName(className);
            Constructor<?> constructor = serviceClass.getConstructor(String.class, ServicesController.class, boolean.class);
            Object instance = constructor.newInstance(name, this, enableLoggingService);
            if (instance instanceof IServices) {
                // check if service already exists
                for (IServices s : servicesList) {
                    if (s.getServiceName().equals(name)) {
                        throw new IllegalArgumentException("Service name already exists");
                    }
                }
                servicesList.add((IServices) instance);
            } else {
                throw new IllegalArgumentException(className + " does not implement IServices");
            }
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException(className + " not found");
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException(className + " does not have a constructor that takes a String parameter");
        } catch (Exception e) {
            if (e instanceof IllegalArgumentException)
                throw new IllegalArgumentException(e.getMessage());
            else
                throw new RuntimeException(e);

        }
    }

    public IServices getService(String name) {
        for (IServices service : servicesList) {
            if (service.getServiceName().equals(name)) {
                return service;
            }
        }
        return null;
    }

    @Override
    public boolean create(Object object) {
        // not used for ServicesController
        throw new UnsupportedOperationException();
    }

    @Override
    public void update(Object object) {
        // not used for ServicesController
        throw new UnsupportedOperationException();
    }

    @Override
    public void delete() {
        // not used for ServicesController
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Object> read() {
        return new ArrayList<>(servicesList);
    }
}
