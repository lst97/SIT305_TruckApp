package com.example.truckapp.models.user;

import com.example.truckapp.models.IModel;

public class UserRole implements IModel {
    private int id;
    private String roleName;
    private String description;

    public UserRole(int id, String roleName, String description) {
        this.id = id;
        this.roleName = roleName;
        this.description = description;
    }

    // Getters and setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
