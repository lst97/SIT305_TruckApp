package com.example.truckapp.services.authenticate;

import com.example.truckapp.models.user.User;
import com.example.truckapp.services.IServices;

public interface IAuthenticateService extends IServices {
    User login(User userCredentials);
    boolean register(User userCredentials);
    void logout();
}
