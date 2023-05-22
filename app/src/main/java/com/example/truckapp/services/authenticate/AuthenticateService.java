package com.example.truckapp.services.authenticate;

import com.example.truckapp.handlers.RepositoryHandler;
import com.example.truckapp.handlers.ServicesHandler;
import com.example.truckapp.models.user.User;
import com.example.truckapp.persistence.UserRepository;
import com.example.truckapp.services.ServiceFactory;
import com.example.truckapp.services.cookie.CookieService;
import com.example.truckapp.services.log.LoggingService;

import org.mindrot.jbcrypt.BCrypt;

public class AuthenticateService implements AuthenticateServiceFactory, ServiceFactory {
    private String serviceName;
    private LoggingService loggingService;
    private CookieService cookieService;
    private UserRepository userRepository;

    public AuthenticateService() {

        onCreate();
    }

    private void onCreate() {
        loggingService = (LoggingService) ServicesHandler.getInstance().getService("LoggingService");
        cookieService = (CookieService) ServicesHandler.getInstance().getService("CookieService");
        userRepository = (UserRepository) RepositoryHandler.getInstance().getRepository("UserRepository");
        if (loggingService == null) {
            throw new RuntimeException("LoggingService is not registered");
        }
        if (cookieService == null) {
            throw new RuntimeException("CookieService is not registered");
        }
    }

    public boolean isSignedIn() {
        return cookieService.getUserSession() != null;
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
        this.loggingService = loggingService;
    }

    @Override
    public User login(User user) {
        // Refactor
        User userFromDatabase = userRepository.read(user.getUsername());

        if (userFromDatabase != null) {
            // user exists
            // check password
            if (validatePassword(user.getPassword(), userFromDatabase.getPassword())) {
                // password is correct
                // set cookie
                cookieService.addUserSession(userFromDatabase);
                return userFromDatabase;
            }
        }
        return null;
    }

    public boolean validatePassword(String password, String hashedPassword) {
        return BCrypt.checkpw(password, hashedPassword);
    }

    @Override
    public boolean register(User user) {
        return userRepository.create(user);
    }

    @Override
    public void logout() {
        CookieService cookieService = (CookieService) ServicesHandler.getInstance().getService("CookieService");
        cookieService.removeUserSession();
    }

    public boolean isUserExist(String username) {
        return userRepository.read(username) != null;
    }
}
