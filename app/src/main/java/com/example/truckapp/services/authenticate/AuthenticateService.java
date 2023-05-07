package com.example.truckapp.services.authenticate;

import com.example.truckapp.controllers.ServicesController;
import com.example.truckapp.models.user.User;
import com.example.truckapp.persistence.DbContext;
import com.example.truckapp.services.IServices;
import com.example.truckapp.services.cookie.CookieService;
import com.example.truckapp.services.log.LoggingService;

import org.mindrot.jbcrypt.BCrypt;

import kotlin.NotImplementedError;

public class AuthenticateService implements IAuthenticateService, IServices {
    private final DbContext dbContext;
    private String serviceName;
    private ServicesController servicesController;
    private LoggingService loggingService;
    private boolean status = false;

    public AuthenticateService(String name, ServicesController servicesCtrl, boolean useLoggingService) {
        servicesController = servicesCtrl;
        if (useLoggingService) {
            loggingService = (LoggingService) servicesController.getService(name);
        }

        serviceName = name;
        dbContext = (DbContext) servicesController.getService("DatabaseService");
        start();
    }

    public int start() {
        status = true;
        return 0;
    }

    public int stop() {
        status = false;
        return 0;
    }

    public boolean isSignedIn() {
        CookieService cookieService = (CookieService) servicesController.getService("CookieService");
        if (cookieService == null) {
            throw new RuntimeException("CookieService is not registered");
        }
        return cookieService.getUserSession() != null;
    }

    @Override
    public String getServiceName() {
        return serviceName;
    }

    @Override
    public User login(User user) {
        if (status) {
            User userCredential = dbContext.validateUser(user.getUsername());
            ;

            // null if user does not exist
            //
            if (userCredential != null) {
                // user exists
                // check password
                User MappedUserCredential = dbContext.validateUserCredential(user.getUsername(), user.getPassword());
                if (MappedUserCredential == null) {
                    // user exists and password is incorrect
                    // password will set to ""
                    return userCredential;
                }
                // Mapped userCredential
                return MappedUserCredential;
            }
            return null;
        }
        // server is not running
        return null;
    }

    public boolean validatePassword(String password, String hashedPassword) {
        return BCrypt.checkpw(password, hashedPassword);
    }

    @Override
    public boolean register(User user) {
        return dbContext.registerUser(user);
    }

    @Override
    public void logout() {
        throw new NotImplementedError("Not implemented");
    }

    public boolean isUserExist(String username) {
        return dbContext.validateUser(username) != null;
    }
}
