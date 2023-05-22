package com.example.truckapp.services.cookie;

import com.example.truckapp.models.user.User;
import com.example.truckapp.services.ServiceFactory;

public interface CookieFactory extends ServiceFactory {
    void addUserSession(User user);

    User getUserSession();

    void removeUserSession();
}
