package com.example.jaqweatherapp;

import java.util.HashMap;
import java.util.Map;

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
    private static final Map<String, Unit> lookup = new HashMap<>();
    static {
        for (Unit unit : Unit.values()) {
           lookup.put(unit.label, unit);
        }
    }
    Unit(String label) {
        this.label = label;
    }
    @Override
    public String toString() {
        return label;
    }
    public static Unit get(String unit) {
        return lookup.get(unit);
    }
}
