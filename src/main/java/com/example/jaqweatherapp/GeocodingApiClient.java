package com.example.jaqweatherapp;

public class GeocodingApiClient extends ApiClient {
    protected GeocodingApiClient() {
        super("https://nominatim.openstreetmap.org/");
    }

}
