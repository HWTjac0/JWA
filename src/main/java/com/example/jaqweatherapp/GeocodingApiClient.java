package com.example.jaqweatherapp;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URLEncoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class GeocodingApiClient extends ApiClient {
    protected GeocodingApiClient() {
        super("https://nominatim.openstreetmap.org/");
    }
    public CompletableFuture<List<Location>> getAddresses(String address) {
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
}
