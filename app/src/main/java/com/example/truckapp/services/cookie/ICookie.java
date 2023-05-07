package com.example.truckapp.services.cookie;

import android.content.SharedPreferences;

import com.example.truckapp.models.user.User;
import com.example.truckapp.services.IServices;

public interface ICookie extends IServices {
    void addUserSession(User user);

    User getUserSession();

    void removeUserSession();

    // Like normal shared preferences
    void addCookie(String name);

    void removeCookie();

    SharedPreferences getCookie(String name);
}
