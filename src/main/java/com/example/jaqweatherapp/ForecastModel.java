package com.example.jaqweatherapp;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@JsonDeserialize(using = ForecastModelDeserializer.class)
public class ForecastModel {
    public HashMap<String, DataSeries> dataMap = new HashMap<>();
    public List<String> dateSeries = new ArrayList<>();

    @JsonIgnore
    public static String getFilterLabel(String filter) {
        return switch (filter) {
            case "windspeed_10m" -> "Prędkość wiatru (10m)";
            case "temperature2m" -> "Temperatura (2m)";
            case "rain" -> "Deszcze";
            case "showers" -> "Przelotne opady";
            case "surfacepressure" -> "Ciśnienie  powierzch";
            default -> throw new IllegalStateException("Unexpected value: " + filter);
        };
    }
}
