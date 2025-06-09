package com.example.jaqweatherapp;

import java.util.HashMap;
import java.util.Map;

public class FilterModel {
    public UnitManager unitManager = new UnitManager();
    public static Map<String, String> filterNameMap = Map.ofEntries(
            Map.entry( "temperature_2m","Temperatura (2m)"),
            Map.entry( "relative_humidity_2m","Względna wilgotność (2m)"),
            Map.entry( "rain","Deszcz"),
            Map.entry( "showers","Przelotne opady"),
            Map.entry( "snowfall","Opady śniegu"),
            Map.entry( "surface_pressure","Ćiśnienie przy powierzchni"),
            Map.entry( "soil_temperature_0cm","Temperatura gleby"),
            Map.entry( "wind_speed_10m","Prędkość wiatru (10m)"),
            Map.entry( "cloud_cover","Zachmurzenie")
    );
    public FilterOption[] filters = new FilterOption[] {
            new FilterOption(filterNameMap.get("temperature_2m"),"temperature_2m", UnitType.TEMPERATURE),
            new FilterOption(filterNameMap.get("relative_humidity_2m"), "relative_humidity_2m", UnitType.PERCENTAGE),
            new FilterOption(filterNameMap.get("rain"), "rain", UnitType.PRECIPITATION),
            new FilterOption(filterNameMap.get("showers"), "showers", UnitType.PRECIPITATION),
            new FilterOption(filterNameMap.get("snowfall"), "snowfall", UnitType.PRECIPITATION),
            new FilterOption(filterNameMap.get("surface_pressure"), "surface_pressure", UnitType.PRESSURE),
            new FilterOption(filterNameMap.get("soil_temperature_0cm"), "soil_temperature_0cm", UnitType.TEMPERATURE),
            new FilterOption(filterNameMap.get("wind_speed_10m"), "wind_speed_10m", UnitType.SPEED),
            new FilterOption(filterNameMap.get("cloud_cover"),"cloud_cover", UnitType.PERCENTAGE),
    };
    public static String getFilterDisplay(String filterName){
        return filterNameMap.get(filterName);
    }
}
