package com.example.truckapp.services.authenticate;

public interface IAccessToken {
    String getToken();

    void dispose();
}
