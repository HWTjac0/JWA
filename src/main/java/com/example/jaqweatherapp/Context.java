package com.example.jaqweatherapp;

public class Context {
    private static Context context = new Context();
    private FilterModel filterModel = new FilterModel();
    private WeatherApiClient weatherApiClient = new WeatherApiClient();
    private GeocodingApiClient geocodingApiClient = new GeocodingApiClient();
    private SearchModel searchModel = new SearchModel();

    public static Context getInstance() {
        return context;
    }
    public FilterModel getDataSearchModel() {
        return filterModel;
    }
    public WeatherApiClient getWeatherApiClient() { return weatherApiClient; }
    public GeocodingApiClient getGeocodingApiClient() { return geocodingApiClient; }
    public SearchModel getSearchModel() { return searchModel; }
}
