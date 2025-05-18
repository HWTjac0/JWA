package com.example.jaqweatherapp;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@JsonDeserialize(using = ForecastModelDeserializer.class)
public class ForecastModel {
    public HashMap<String, DataSeries> data = new HashMap<>();
    public List<String> dateSeries = new ArrayList<>();
}
