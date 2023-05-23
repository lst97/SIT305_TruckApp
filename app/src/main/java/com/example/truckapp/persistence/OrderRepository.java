package com.example.truckapp.persistence;

import com.example.truckapp.handlers.ServicesHandler;
import com.example.truckapp.helpers.PostgresDatabaseHelper;
import com.example.truckapp.models.order.Order;
import com.example.truckapp.models.truck.TruckTypes;
import com.example.truckapp.services.authenticate.AccessToken;
import com.example.truckapp.services.authenticate.AuthenticateService;
import com.example.truckapp.services.log.LogTypes;
import com.example.truckapp.services.log.LoggingService;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class OrderRepository implements RepositoryFactory<Order> {
    private final LoggingService loggingService;
    private PostgresDatabaseHelper postgresDatabaseHelper = null;
    private AccessToken accessToken = null;
    private String repositoryName;

    public OrderRepository(PostgresDatabaseHelper postgresDatabaseHelper) {
        this.postgresDatabaseHelper = postgresDatabaseHelper;
        loggingService = (LoggingService) ServicesHandler.getInstance().getService("LoggingService");
    }

    @Override
    public boolean create(Order order) {
        if (!isPermitted()) {
            return false;
        }

        PreparedStatement stmt = null;

        Connection connection = postgresDatabaseHelper.Connect();
        try {
            String timeString;
            try {
                timeString = String.valueOf(Time.valueOf(order.getPickupTime().toString()));
            } catch (IllegalArgumentException e) {
                timeString = String.valueOf(Time.valueOf(order.getPickupTime().toString() + ":00"));
            }

            stmt = connection.prepareStatement(
                    "INSERT INTO orders (user_id, vehicle_id, receiver_name, pickup_date, pickup_time, pickup_location, good_type, weight, width, length, height, vehicle_type) " +
                            "VALUES (?,?,?,?,?,?,?,?,?,?,?,?)");
            stmt.setInt(1, order.getUserId());
            stmt.setInt(2, order.getVehicleId());
            stmt.setString(3, order.getReceiverName());
            stmt.setDate(4, Date.valueOf(order.getPickupDate().toString()));
            stmt.setTime(5, Time.valueOf(timeString));
            stmt.setString(6, order.getPickupLocation());
            stmt.setString(7, order.getGoodType());
            stmt.setDouble(8, order.getWeight());
            stmt.setDouble(9, order.getWidth());
            stmt.setDouble(10, order.getLength());
            stmt.setDouble(11, order.getHeight());
            stmt.setInt(12, TruckTypes.getTruckTypeId(order.getVehicleType()));
            stmt.executeUpdate();

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            // close connection
            postgresDatabaseHelper.Disconnect(stmt, null);
        }
    }

    @Override
    public Order read(int id) {
        return null;
    }

    public List<Order> getOrdersByUserId(int userId) {
        if (!isPermitted()) {
            return null;
        }
        List<Order> orders = new ArrayList<>();
        PreparedStatement stmt = null;
        ResultSet rs = null;

        Connection connection = postgresDatabaseHelper.Connect();
        try {
            stmt = connection.prepareStatement(
                    "SELECT * FROM orders WHERE user_id = ?"
            );
            stmt.setInt(1, userId);
            rs = stmt.executeQuery();

            while (rs.next()) {
                orders.add(new Order(
                        rs.getInt("id"),
                        rs.getInt("user_id"),
                        rs.getInt("vehicle_id"),
                        rs.getString("receiver_name"),
                        LocalDate.parse(rs.getDate("pickup_date").toString()),
                        LocalTime.parse(rs.getTime("pickup_time").toString()),
                        rs.getString("pickup_location"),
                        rs.getString("good_type"),
                        rs.getDouble("weight"),
                        rs.getDouble("width"),
                        rs.getDouble("length"),
                        rs.getDouble("height"),
                        TruckTypes.getTruckTypeName(rs.getInt("vehicle_type"))
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            postgresDatabaseHelper.Disconnect(stmt, rs);
        }
        return orders;
    }

    @Override
    public Order read(String name) {
        return null;
    }

    @Override
    public List<Order> readAll() {
        return null;
    }

    @Override
    public boolean update(Order order) {
        return false;
    }

    @Override
    public boolean delete(int id) {
        return false;
    }

    @Override
    public boolean delete(String name) {
        return false;
    }

    @Override
    public String getRepositoryName() {
        return this.repositoryName;
    }

    @Override
    public void setRepositoryName(String repositoryName) {
        this.repositoryName = repositoryName;
    }

    @Override
    public void setAccessToken(AccessToken accessToken) {
        this.accessToken = accessToken;
    }

    private boolean isPermitted() {
        // check if the user is logged in
        // Simulate backend authentication
        AuthenticateService authenticateService = (AuthenticateService) ServicesHandler.getInstance().getService("AuthenticateService");
        if (authenticateService == null) {
            throw new RuntimeException("AuthenticateService is not registered");
        }
        if (!authenticateService.isSignedIn()) {
            if (Objects.equals(accessToken.getToken(), "")) {
                throw new RuntimeException("User is not signed in");
            } else {
                loggingService.log("Access using one time token", LogTypes.INFO);
                // token can only be used once
                accessToken.dispose();
                return true;
            }
        }
        return true;
    }

    public Order getOrderByTruckId(int id) {
        if (!isPermitted()) {
            return null;
        }
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Connection connection = postgresDatabaseHelper.Connect();
        try {
            stmt = connection.prepareStatement("SELECT * FROM orders WHERE vehicle_id = ?");
            stmt.setInt(1, id);
            rs = stmt.executeQuery();

            if (rs.next()) {
                return new Order(
                        rs.getInt("id"),
                        rs.getInt("user_id"),
                        rs.getInt("vehicle_id"),
                        rs.getString("receiver_name"),
                        LocalDate.parse(rs.getDate("pickup_date").toString()),
                        LocalTime.parse(rs.getTime("pickup_time").toString()),
                        rs.getString("pickup_location"),
                        rs.getString("good_type"),
                        rs.getDouble("weight"),
                        rs.getDouble("width"),
                        rs.getDouble("length"),
                        rs.getDouble("height"),
                        TruckTypes.getTruckTypeName(rs.getInt("vehicle_type"))
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            postgresDatabaseHelper.Disconnect(stmt, rs);
        }
        return null;
    }
}
