package com.example.jaqweatherapp;

import java.util.prefs.Preferences;

public class Context {
    private static final Context context = new Context();
    private final FilterModel filterModel = new FilterModel();
    private final WeatherApiClient weatherApiClient = new WeatherApiClient();
    private final GeocodingApiClient geocodingApiClient = new GeocodingApiClient();
    private final SearchModel searchModel = new SearchModel();
    private final ForecastModel forecastModel = new ForecastModel();
    private final DateRangeModel dateRangeModel = new DateRangeModel();
    private final CacheManager cacheManager = new CacheManager();
    private final SettingsModel settingsModel = new SettingsModel();

    public static Context getInstance() {
        return context;
    }
    public FilterModel getFilterModel() {
        return filterModel;
    }
    public WeatherApiClient getWeatherApiClient() { return weatherApiClient; }
    public GeocodingApiClient getGeocodingApiClient() { return geocodingApiClient; }
    public SearchModel getSearchModel() { return searchModel; }
    public ForecastModel getForecastModel() { return forecastModel; }
    public DateRangeModel getDateRangeModel() { return dateRangeModel; }
    public CacheManager getCacheManager() { return cacheManager; }
    public SettingsModel getSettingsModel() { return settingsModel; }
}
