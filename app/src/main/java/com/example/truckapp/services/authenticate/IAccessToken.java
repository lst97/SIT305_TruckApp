package com.example.truckapp.services.authenticate;

import java.util.UUID;

public interface IAccessToken {
    String getToken();
    void dispose();
}
