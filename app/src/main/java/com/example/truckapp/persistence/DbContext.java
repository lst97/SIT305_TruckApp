package com.example.truckapp.persistence;

import com.example.truckapp.controllers.ServicesController;
import com.example.truckapp.models.order.Order;
import com.example.truckapp.models.truck.Truck;
import com.example.truckapp.models.truck.TruckTypes;
import com.example.truckapp.models.user.User;
import com.example.truckapp.services.IServices;
import com.example.truckapp.services.authenticate.AccessToken;
import com.example.truckapp.services.authenticate.AuthenticateService;
import com.example.truckapp.services.log.LogTypes;
import com.example.truckapp.services.log.LoggingService;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DbContext implements IServices {
    private static final String HOST = "10.0.2.2";
    private static final String PORT = "5432";
    private static final String DB_NAME = "sit305";
    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "password";
    private static final String url = "jdbc:postgresql://" + HOST + ":" + PORT + "/" + DB_NAME;
    // PostgresSQL
    private static String serviceName = "DatabaseService";
    private final ServicesController servicesController;
    public AccessToken accessToken;
    private LoggingService loggingService = null;
    private Connection connection;
    private boolean status;
    public DbContext(String serviceName, ServicesController servicesCtrl, boolean useLoggingService) throws SQLException {
        servicesController = servicesCtrl;
        if (useLoggingService) {
            loggingService = (LoggingService) servicesController.getService("LoggingService");
        }
        DbContext.serviceName = serviceName;
        start();
    }

    public void setAccessToken(AccessToken accessToken) {
        this.accessToken = accessToken;
    }

    private void OnConfiguring() throws SQLException {
        // Test connection
        Connect();
        // use LoggingService to log the connection status
        if (loggingService != null) {
            if (connection != null) {
                loggingService.log("Database connection status: " + true, LogTypes.INFO);
            } else {
                loggingService.log("Database connection status: " + false, LogTypes.ERROR);
            }
        }
        connection.close();
        connection = null;
    }

    private void Connect() {
        if (connection == null) {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Class.forName("org.postgresql.Driver");
                        connection = DriverManager.getConnection(url, USERNAME, PASSWORD);
                        System.out.println("connected:" + true);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            thread.start();
            try {
                thread.join();
            } catch (Exception e) {
                e.printStackTrace();
                this.status = false;
            }
        }
    }

    private boolean Disconnect() {
        try {
            connection.close();
            connection = null;
            loggingService.log("Disconnected", LogTypes.INFO);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void Disconnect(PreparedStatement stmt, ResultSet rs) {
        try {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            if (connection != null) connection.close();
            connection = null;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean Execute() {
        return false;
    }

    public User validateUser(String username) {
        User user = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            Connect();
            stmt = connection.prepareStatement("SELECT * FROM users WHERE username = ?");
            stmt.setString(1, username);
            rs = stmt.executeQuery();

            if (rs.next()) {
                user = new User(
                        rs.getInt("id"),
                        rs.getString("username")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // close connection
            Disconnect(stmt, rs);
        }
        return user;
    }

    public boolean registerUser(User userCredential) {
        PreparedStatement stmt = null;
        try {
            Connect();
            stmt = connection.prepareStatement("INSERT INTO users (username, password, created_date, modified_date, phone_number, role_id) VALUES (?, ?, ?, ?, ?, ?)");
            stmt.setString(1, userCredential.getUsername());
            stmt.setString(2, userCredential.getPassword());
            stmt.setDate(3, Date.valueOf(String.valueOf(userCredential.getCreatedDate().toLocalDate())));
            stmt.setDate(4, Date.valueOf(String.valueOf(userCredential.getModifiedDate().toLocalDate())));
            stmt.setString(5, userCredential.getPhoneNumber());
            stmt.setInt(6, userCredential.getRoleId());
            stmt.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            // close connection
            Disconnect(stmt, null);
        }
    }

    public void deleteUser(String name) {
        PreparedStatement stmt = null;
        try {
            Connect();
            stmt = connection.prepareStatement("DELETE FROM users WHERE username = ?");
            stmt.setString(1, name);
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // close connection
            Disconnect(stmt, null);
        }
    }

    public User validateUserCredential(String username, String hashedPassword) {
        User user = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            AuthenticateService authenticateService = (AuthenticateService) servicesController.getService("AuthenticateService");
            if (authenticateService == null) {
                throw new Exception("AuthenticateService is not registered");
            }

            Connect();
            stmt = connection.prepareStatement("SELECT * FROM users WHERE username = ?");
            stmt.setString(1, username);
            rs = stmt.executeQuery();

            if (rs.next()) {
                // Get the hashed password from the database
                String dbHashedPassword = rs.getString("password");

                if (authenticateService.validatePassword(hashedPassword, dbHashedPassword)) {
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
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            // close connection
            Disconnect(stmt, rs);
        }
        return user;
    }

    public Truck getTruck(int id) {
        if (!isPermitted()) {
            return null;
        }

        Truck truck = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            Connect();
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
            // close connection
            Disconnect(stmt, rs);
        }

        return truck;
    }

    public boolean createOrder(Order order) {
        if (!isPermitted()) {
            return false;
        }

        PreparedStatement stmt = null;

        try {
            Connect();
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
            Disconnect(stmt, null);
        }
    }

    public void updateTruck(Truck truck) {
        if (!isPermitted()) {
            return;
        }

        PreparedStatement stmt = null;
        ResultSet rs = null;

        // Update the truck
        try {
            Connect();
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
            Disconnect(stmt, rs);
        }
    }

    public Truck getTruckByName(String name) {
        if (!isPermitted()) {
            return null;
        }

        Truck truck = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            Connect();
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
            Disconnect(stmt, rs);
        }

        return truck;
    }

    private boolean isPermitted() {
        // check if the user is logged in
        // Simulate backend authentication
        AuthenticateService authenticateService = (AuthenticateService) servicesController.getService("AuthenticateService");
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

    public List<Truck> getTrucks() {

        if (!isPermitted()) {
            return null;
        }

        List<Truck> trucks = new ArrayList<>();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            Connect();
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
            Disconnect(stmt, rs);
        }

        return trucks;
    }

    public void Query(String query) {

    }

    @Override
    public int start() throws SQLException {
        OnConfiguring();
        status = true;
        return 0;
    }

    @Override
    public int stop() {
        if (Disconnect()) {
            status = false;
            return 0;
        }
        return -1;
    }

    @Override
    public String getServiceName() {
        return serviceName;
    }

    public Truck getTruckById(int id) {
        if (!isPermitted()) {
            return null;
        }
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            Connect();
            stmt = connection.prepareStatement("SELECT * FROM trucks WHERE id = ?");
            stmt.setInt(1, id);
            rs = stmt.executeQuery();

            if (rs.next()) {
                return new Truck(
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
            Disconnect(stmt, rs);
        }
        return null;
    }

    public Order getOrderByTruckId(int id) {
        if (!isPermitted()) {
            return null;
        }
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            Connect();
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
            Disconnect(stmt, rs);
        }
        return null;
    }

    public List<Order> getTruckOrders(User user) {
        if (!isPermitted()) {
            return null;
        }
        List<Order> orders = new ArrayList<>();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            Connect();
            stmt = connection.prepareStatement(
                    "SELECT * FROM orders WHERE user_id = ?"
            );
            stmt.setInt(1, user.getId());
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
            Disconnect(stmt, rs);
        }
        return orders;
    }

    public List<Truck> getAvailableTrucks() {
        if (!isPermitted()) {
            return null;
        }

        List<Truck> trucks = new ArrayList<>();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            Connect();
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
            Disconnect(stmt, rs);
        }

        return trucks;
    }
}
