package com.example.jaqweatherapp;

import java.util.EnumMap;
import java.util.Map;

public class UnitManager {
    public Map<UnitType, Unit> currentUnits = new EnumMap<>(UnitType.class);

    public UnitManager() {
        currentUnits.put(UnitType.PERCENTAGE, Unit.Percent);
        currentUnits.put(UnitType.SPEED, Unit.KmPerHour);
        currentUnits.put(UnitType.TEMPERATURE, Unit.Celsius);
        currentUnits.put(UnitType.PRECIPITATION, Unit.Millimeters);
        currentUnits.put(UnitType.PRESSURE, Unit.Hectopascal);
    }
}
