package com.example.truckapp.services.authenticate;
import java.util.UUID;

// For the DB access without the need of user authentication
// Can only be used once
public class AccessToken implements IAccessToken {
    @Override
    public String getToken() {
        return token;
    }
    private String token;

    public AccessToken() {
        this.token = UUID.randomUUID().toString();
    }

    @Override
    public void dispose() {
        token = null;
    }
}
