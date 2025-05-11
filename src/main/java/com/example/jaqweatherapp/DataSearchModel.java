package com.example.jaqweatherapp;

import javafx.util.Pair;

public class DataSearchModel {
    public FilterOption[] filters = new FilterOption[] {
            new FilterOption("Temperatura (2m)", "temperature_2m", UnitType.TEMPERATURE),
            new FilterOption("Względna wilgotność (2m)", "relative_humidity_2m", UnitType.PERCENTAGE),
            new FilterOption("Deszcz", "rain", UnitType.PRECIPITATION),
            new FilterOption("Przelotne opady", "showers", UnitType.PRECIPITATION),
            new FilterOption("Opady śniegu", "snowfall", UnitType.PRECIPITATION),
            new FilterOption("Ćiśnienie przy powierzchni", "surface_pressure", UnitType.PRESSURE),
            new FilterOption("Temperatura gleby", "soil_temperature_0cm", UnitType.TEMPERATURE),
            new FilterOption("Prędkość wiatru (10m)", "wind_speed_10m", UnitType.SPEED),
            new FilterOption("Zachmurzenie", "cloud_cover", UnitType.PERCENTAGE),
    };
}
