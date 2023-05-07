package com.example.truckapp.controllers;

import java.util.List;

public interface IController {
    boolean create(Object object);

    void update(Object object);

    void delete();

    // return list of objects
    List<Object> read();
}
