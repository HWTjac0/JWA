package com.example.jaqweatherapp;

public enum UnitType {
    TEMPERATURE("temperature_unit"),
    PERCENTAGE(""),
    PRECIPITATION("precipation_unit"),
    PRESSURE(""),
    SPEED("wind_speed_unit");

    private final String label;
    private UnitType(String label) {
        this.label = label;
    }
    @Override
    public String toString() {
        return label;
    }
}
