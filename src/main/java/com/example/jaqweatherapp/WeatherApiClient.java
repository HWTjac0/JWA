package com.example.jaqweatherapp;

import java.util.Map;

public class WeatherApiClient extends ApiClient {
    protected WeatherApiClient() {
        super("open meteo");
    }
    @Override
    protected String buildQueryString(Map<String, String> parameters) {
        for(String key : parameters.keySet()) {
            String param = key + "=";
            switch(key) {
                case "hourly":
                break;
            }
        }
        return "";
    }
}
