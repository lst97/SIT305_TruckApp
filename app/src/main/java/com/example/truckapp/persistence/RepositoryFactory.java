package com.example.truckapp.persistence;

import com.example.truckapp.services.authenticate.AccessToken;

import java.util.List;

public interface RepositoryFactory<T> {
    // CRUD

    // Create
    boolean create(T item);

    // Read
    T read(int id);

    T read(String name);

    List<T> readAll();

    // Update
    boolean update(T item);

    // Delete
    boolean delete(int id);

    boolean delete(String name);

    String getRepositoryName();

    void setRepositoryName(String repositoryName);

    void setAccessToken(AccessToken accessToken);


}
