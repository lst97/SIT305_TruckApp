package com.example.truckapp.utils;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordHasher {
    // Generally recommended to hash the password on the server-side
    public static String hashPassword(String password) {
        try {
            // Generate a salt for the password hash
            String salt = BCrypt.gensalt();

            return BCrypt.hashpw(password, salt);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
