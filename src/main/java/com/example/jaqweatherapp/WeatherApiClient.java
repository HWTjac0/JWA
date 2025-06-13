package com.example.jaqweatherapp;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class WeatherApiClient extends ApiClient {
    protected WeatherApiClient() {
        super("https://api.open-meteo.com/v1");
    }
    public CompletableFuture<String> getForecast(Map<String, String> parameters) throws SocketTimeoutException, ConnectException {
        return sendGETRequest("/forecast", parameters);
    }
}
