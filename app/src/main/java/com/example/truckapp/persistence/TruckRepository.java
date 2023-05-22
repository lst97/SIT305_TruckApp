package com.example.truckapp.persistence;

import com.example.truckapp.handlers.PostgresDatabaseHelper;
import com.example.truckapp.handlers.ServicesHandler;
import com.example.truckapp.models.truck.Truck;
import com.example.truckapp.services.authenticate.AccessToken;
import com.example.truckapp.services.authenticate.AuthenticateService;
import com.example.truckapp.services.log.LogTypes;
import com.example.truckapp.services.log.LoggingService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TruckRepository implements RepositoryFactory<Truck> {
    private final LoggingService loggingService;
    private PostgresDatabaseHelper postgresDatabaseHelper = null;
    private AccessToken accessToken;
    private String repositoryName;

    public TruckRepository(PostgresDatabaseHelper postgresDatabaseHelper) {
        this.postgresDatabaseHelper = postgresDatabaseHelper;
        loggingService = (LoggingService) ServicesHandler.getInstance().getService("LoggingService");
    }

    public void setAccessToken(AccessToken accessToken) {
        this.accessToken = accessToken;
    }

    @Override
    public boolean create(Truck item) {
        // not used
        return true;
    }

    @Override
    public Truck read(int id) {
        if (!isPermitted()) {
            return null;
        }

        Truck truck = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        Connection connection = postgresDatabaseHelper.Connect();
        try {
            stmt = connection.prepareStatement("SELECT * FROM trucks WHERE id = ?");
            stmt.setInt(1, id);
            rs = stmt.executeQuery();

            while (rs.next()) {
                truck = new Truck(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getString("title"),
                        rs.getString("image"),
                        rs.getDouble("price"),
                        rs.getInt("type"),
                        rs.getString("location"),
                        rs.getDouble("latitude"),
                        rs.getDouble("longitude")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            postgresDatabaseHelper.Disconnect(stmt, rs);
        }

        return truck;
    }

    @Override
    public Truck read(String name) {
        if (!isPermitted()) {
            return null;
        }

        Truck truck = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        Connection connection = postgresDatabaseHelper.Connect();

        try {
            stmt = connection.prepareStatement("SELECT * FROM trucks WHERE name = ?");
            stmt.setString(1, name);
            rs = stmt.executeQuery();

            while (rs.next()) {
                truck = new Truck(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getString("title"),
                        rs.getString("image"),
                        rs.getDouble("price"),
                        rs.getInt("type"),
                        rs.getString("location"),
                        rs.getDouble("latitude"),
                        rs.getDouble("longitude")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // close connection
            postgresDatabaseHelper.Disconnect(stmt, rs);
        }

        return truck;
    }

    @Override
    public List<Truck> readAll() {
        if (!isPermitted()) {
            return null;
        }

        List<Truck> trucks = new ArrayList<>();
        PreparedStatement stmt = null;
        ResultSet rs = null;

        Connection connection = postgresDatabaseHelper.Connect();
        try {
            stmt = connection.prepareStatement("SELECT * FROM trucks");
            rs = stmt.executeQuery();

            //name, description, title, image, price, type, location, latitude, longitude
            while (rs.next()) {
                trucks.add(new Truck(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getString("title"),
                        rs.getString("image"),
                        rs.getDouble("price"),
                        rs.getInt("type"),
                        rs.getString("location"),
                        rs.getDouble("latitude"),
                        rs.getDouble("longitude")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // close connection
            postgresDatabaseHelper.Disconnect(stmt, rs);
        }

        return trucks;
    }

    @Override
    public boolean update(Truck truck) {
        if (!isPermitted()) {
            return false;
        }

        PreparedStatement stmt = null;
        ResultSet rs = null;

        Connection connection = postgresDatabaseHelper.Connect();
        try {
            stmt = connection.prepareStatement("UPDATE trucks SET name = ?, description = ?, title = ?, image = ?, price = ?, type = ?, location = ?, latitude = ?, longitude = ? WHERE id = ?");
            stmt.setString(1, truck.getName());
            stmt.setString(2, truck.getDescription());
            stmt.setString(3, truck.getTitle());
            stmt.setString(4, truck.getImage());
            stmt.setDouble(5, truck.getPrice());
            stmt.setInt(6, truck.getType());
            stmt.setString(7, truck.getLocation());
            stmt.setDouble(8, truck.getLatitude());
            stmt.setDouble(9, truck.getLongitude());
            stmt.setInt(10, truck.getId());
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // close connection
            postgresDatabaseHelper.Disconnect(stmt, rs);
        }
        return true;
    }

    @Override
    public boolean delete(int id) {
        // not used
        return false;
    }

    @Override
    public boolean delete(String name) {
        // not used
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

    public List<Truck> getAvailableTrucks() {
        if (!isPermitted()) {
            return null;
        }

        List<Truck> trucks = new ArrayList<>();
        PreparedStatement stmt = null;
        ResultSet rs = null;

        Connection connection = postgresDatabaseHelper.Connect();
        try {
            stmt = connection.prepareStatement("" +
                    "SELECT t.*\n" +
                    "FROM trucks t\n" +
                    "LEFT JOIN orders o ON t.id = o.vehicle_id\n" +
                    "WHERE o.vehicle_id IS NULL;"
            );
            rs = stmt.executeQuery();

            while (rs.next()) {
                trucks.add(new Truck(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getString("title"),
                        rs.getString("image"),
                        rs.getDouble("price"),
                        rs.getInt("type"),
                        rs.getString("location"),
                        rs.getDouble("latitude"),
                        rs.getDouble("longitude")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // close connection
            postgresDatabaseHelper.Disconnect(stmt, rs);
        }

        return trucks;
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
}
