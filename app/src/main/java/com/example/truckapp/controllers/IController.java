package com.example.truckapp.controllers;

import java.util.List;

public interface IController {
    void create();
    void update(Object object);
    void delete();
    // return list of objects
    List<Object> read();
}
