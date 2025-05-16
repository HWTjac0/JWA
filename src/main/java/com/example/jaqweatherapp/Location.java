package com.example.jaqweatherapp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Location {
    @JsonProperty("lat")
    public double latitude;
    @JsonProperty("lon")
    public double longitude;
    @JsonProperty("display_name")
    public String displayName;
    public Location() {}
    public Location(double latitude, double longitude, String displayName) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.displayName = displayName;
    }
}
