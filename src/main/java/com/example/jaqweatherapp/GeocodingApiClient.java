package com.example.jaqweatherapp;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class GeocodingApiClient extends ApiClient {
    protected GeocodingApiClient() {
        super("https://nominatim.openstreetmap.org/");
    }
    public CompletableFuture<List<Location>> getAddresses(String address) throws SocketTimeoutException, ConnectException {
        String encodedAddress = URLEncoder.encode(address);
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("q", encodedAddress);
        parameters.put("format", "json");
        parameters.put("accept-language", "pl");
        ObjectMapper objectMapper = new ObjectMapper();
        return sendGETRequest("/search", parameters)
                .thenApply(reply -> {
                    try {
                        return objectMapper.readValue(reply, new TypeReference<List<Location>>() {});
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                });
    }
    public CompletableFuture<Location> reverseGeocode(Double lat, Double lon) throws SocketTimeoutException, ConnectException {
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("lat", lat.toString());
        parameters.put("lon", lon.toString());
        parameters.put("accept-language", "pl");
        parameters.put("format", "json");
        ObjectMapper objectMapper = new ObjectMapper();
        return sendGETRequest("/reverse", parameters)
                .thenApply(reply -> {
                    try {
                        return objectMapper.readValue(reply, Location.class);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                });
    }
}
