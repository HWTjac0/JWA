package com.example.jaqweatherapp;

import java.util.Map;

public class WeatherApiClient extends ApiClient {
    protected WeatherApiClient() {
        super("https://api.open-meteo.com/v1/forecast");
    }
}
