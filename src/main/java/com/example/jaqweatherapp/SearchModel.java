package com.example.jaqweatherapp;

public class SearchModel {
    public SearchController.SearchType currentSearchType;
    public String locationName = "";
    public Coordinates coordinates = new Coordinates(0,0);
    public boolean areCoordsEmpty = true;
    public boolean isLocationEmpty = true;
    public boolean areCoordsCorrect(){
        double lat = coordinates.latitude;
        double lon = coordinates.longitude;
        return (lat < 90 && lat > -90 && lon < 180 && lon > -180);
    }
}
