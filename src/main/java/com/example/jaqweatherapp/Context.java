package com.example.jaqweatherapp;

public class Context {
    private static Context context = new Context();
    private DataSearchModel dataSearchModel = new DataSearchModel();
    private WeatherApiClient weatherApiClient = new WeatherApiClient();
    private GeocodingApiClient geocodingApiClient = new GeocodingApiClient();

    public static Context getInstance() {
        return context;
    }
    public DataSearchModel getDataSearchModel() {
        return dataSearchModel;
    }
    public WeatherApiClient getWeatherApiClient() { return weatherApiClient; }
    public GeocodingApiClient getGeocodingApiClient() { return geocodingApiClient; }
}
