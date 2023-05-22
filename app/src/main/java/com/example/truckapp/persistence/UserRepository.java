package com.example.truckapp.persistence;

import com.example.truckapp.handlers.PostgresDatabaseHelper;
import com.example.truckapp.handlers.ServicesHandler;
import com.example.truckapp.models.user.User;
import com.example.truckapp.services.authenticate.AccessToken;
import com.example.truckapp.services.log.LogTypes;
import com.example.truckapp.services.log.LoggingService;

import java.sql.Connection;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;


public class UserRepository implements RepositoryFactory<User> {

    private final LoggingService loggingService;
    private PostgresDatabaseHelper postgresDatabaseHelper = null;
    private AccessToken accessToken;
    private String repositoryName;

    public UserRepository(PostgresDatabaseHelper postgresDatabaseHelper) {
        this.postgresDatabaseHelper = postgresDatabaseHelper;
        loggingService = (LoggingService) ServicesHandler.getInstance().getService("LoggingService");

    }

    @Override
    public boolean create(User user) {
        PreparedStatement stmt = null;

        Connection connection = postgresDatabaseHelper.Connect();

        // check if user exists

        try {
            stmt = connection.prepareStatement("SELECT * FROM users WHERE username = ?");
            stmt.setString(1, user.getUsername());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                loggingService.log("User already exists", LogTypes.ERROR);
                return false;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        try {
            stmt = connection.prepareStatement("INSERT INTO users (username, password, created_date, modified_date, phone_number, role_id) VALUES (?, ?, ?, ?, ?, ?)");
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            stmt.setDate(3, Date.valueOf(String.valueOf(user.getCreatedDate().toLocalDate())));
            stmt.setDate(4, Date.valueOf(String.valueOf(user.getModifiedDate().toLocalDate())));
            stmt.setString(5, user.getPhoneNumber());
            stmt.setInt(6, user.getRoleId());
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
    public User read(int id) {
        return null;
    }

    @Override
    public User read(String username) {
        User user = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        Connection connection = postgresDatabaseHelper.Connect();

        try {
            stmt = connection.prepareStatement("SELECT * FROM users WHERE username = ?");
            stmt.setString(1, username);
            rs = stmt.executeQuery();

            if (rs.next()) {
                // Convert the SQL Date to Instant
                user = new User(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("full_name"),
                        rs.getDate("created_date"),
                        rs.getDate("modified_date"),
                        rs.getString("phone_number"),
                        rs.getInt("role_id")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // close connection
            postgresDatabaseHelper.Disconnect(stmt, rs);
        }
        return user;
    }

    @Override
    public List<User> readAll() {
        return null;
    }

    @Override
    public boolean update(User user) {
        return false;
    }

    @Override
    public boolean delete(int id) {
        return false;
    }

    @Override
    public boolean delete(String name) {
        PreparedStatement stmt = null;
        Connection connection = postgresDatabaseHelper.Connect();
        try {
            stmt = connection.prepareStatement("DELETE FROM users WHERE username = ?");
            stmt.setString(1, name);
            stmt.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // close connection
            postgresDatabaseHelper.Disconnect(stmt, null);
        }
        return false;
    }

    @Override
    public String getRepositoryName() {
        return repositoryName;
    }

    @Override
    public void setRepositoryName(String repositoryName) {
        this.repositoryName = repositoryName;
    }

    @Override
    public void setAccessToken(AccessToken accessToken) {
        this.accessToken = accessToken;
    }
}
