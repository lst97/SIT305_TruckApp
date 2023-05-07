package com.example.truckapp.fasterxml.modules;

import com.example.truckapp.fasterxml.deserializers.LocalDateTimeDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;

import java.time.LocalDateTime;

public class LocalDateTimeModule extends SimpleModule {
    public LocalDateTimeModule() {
        addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer());
    }
}