package com.example.truckapp.services.authenticate;

import com.example.truckapp.models.user.User;
import com.example.truckapp.services.ServiceFactory;

public interface AuthenticateServiceFactory extends ServiceFactory {
    User login(User userCredentials);

    boolean register(User userCredentials);

    void logout();
}
