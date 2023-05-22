package com.example.truckapp.handlers;

import com.example.truckapp.services.log.LoggingService;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PostgresDatabaseHelper {
    private static final String HOST = "10.0.2.2";
    private static final String PORT = "5432";
    private static final String DB_NAME = "sit305";
    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "password";
    private static final String url = "jdbc:postgresql://" + HOST + ":" + PORT + "/" + DB_NAME;

    private LoggingService loggingService = null;
    private Connection connection;

    public PostgresDatabaseHelper() {
        onCreated();
    }

    private void onCreated() {
        loggingService = (LoggingService) ServicesHandler.getInstance().getService("LoggingService");
        if (loggingService == null) {
            throw new RuntimeException("LoggingService is not registered");
        }
    }

    public Connection Connect() {
        if (connection == null) {
            Thread thread = new Thread(() -> {
                try {
                    Class.forName("org.postgresql.Driver");
                    connection = DriverManager.getConnection(url, USERNAME, PASSWORD);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            thread.start();
            try {
                thread.join();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return connection;
    }

    public void Disconnect(PreparedStatement stmt, ResultSet rs) {
        try {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            if (connection != null) connection.close();
            connection = null;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
