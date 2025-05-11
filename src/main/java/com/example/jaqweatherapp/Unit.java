package com.example.jaqweatherapp;

public enum Unit {
    Celsius("°C"),
    Fahrenheit("°F"),
    Hectopascal("hPa"),
    KmPerHour("km/h"),
    MPerSecond("m/s"),
    Knots("kn"),
    MilesPerHour("mph"),
    Millimeters("mm"),
    Inches("in"),
    Percent("%");
    private final String label;
    private Unit(String label) {
        this.label = label;
    }
    @Override
    public String toString() {
        return label;
    }
}
