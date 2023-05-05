package com.example.truckapp.models.user;

import com.example.truckapp.models.IModel;

import java.sql.Date;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class User implements IModel {
    private int id;
    private String username;
    private String password;
    private String fullName;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
    private String phoneNumber;
    private int roleId;

    // for login credentials checking
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // for login mapping
    public User(int id, String username, String password, String fullName, Date createdDate, Date modifiedDate, String phoneNumber, int roleId) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.createdDate = createdDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        this.modifiedDate = modifiedDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        this.phoneNumber = phoneNumber;
        this.roleId = roleId;
    }

    // for register
    public User(String username, String password, String fullName, LocalDateTime createdDate, LocalDateTime modifiedDate, String phoneNumber, int roleId) {
        this.id = -1;
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
        this.phoneNumber = phoneNumber;
        this.roleId = roleId;
    }

    // for username checking
    public User(int id, String username) {
        this.id = id;
        this.username = username;
        this.password = "";
    }

    public User() {
    }

    // for user session
    public User(String username, String password, String fullName, String phoneNumber, int roleId) {
        this.id = -1;
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.createdDate = null;
        this.modifiedDate = null;
        this.phoneNumber = phoneNumber;
        this.roleId = roleId;
    }

    // Getters and setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public LocalDateTime getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(LocalDateTime modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}
