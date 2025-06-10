package com.example.jaqweatherapp;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.prefs.Preferences;

public class EvaluateController implements Initializable {
    @FXML Button fetchButton;
    FilterModel filterModel = Context.getInstance().getFilterModel();
    SearchModel searchModel = Context.getInstance().getSearchModel();
    DateRangeModel dateRangeModel = Context.getInstance().getDateRangeModel();
    WeatherApiClient weatherApiClient = Context.getInstance().getWeatherApiClient();
    ForecastModel forecastModel = Context.getInstance().getForecastModel();
    ApiParameters apiParameters = new ApiParameters();
    CacheManager cacheManager = Context.getInstance().getCacheManager();
    Preferences prefs = Preferences.userRoot().node("/com/hwtjac0/JaqWeatherApp");
    public void initialize(URL location, ResourceBundle resources) {
        fetchButton.setOnAction(event -> {
            apiParameters.clear();

            if(!setFilterParams(apiParameters)) return;
            setDataRangeParams(apiParameters);

            apiParameters.add("latitude", String.valueOf(searchModel.coordinates.latitude));
            apiParameters.add("longitude", String.valueOf(searchModel.coordinates.longitude));
            ObjectMapper mapper = new ObjectMapper();
            Task<ForecastModel> forecastTask = new Task<>() {
                @Override
                protected ForecastModel call() throws Exception {
                    forecastModel.dateSeries.clear();
                    forecastModel.dataMap.clear();
                    Result<String> res = cacheManager.getCache(apiParameters.getHash());
                    if(!res.valid()) {
                        CompletableFuture<String> modelPromise = weatherApiClient.getForecast(apiParameters.getParameters());
                        String response = modelPromise.join();
                        cacheManager.setCache(apiParameters.getHash(), response);
                        try {
                            return mapper.readValue(response, ForecastModel.class);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        return mapper.readValue(res.data(), ForecastModel.class);
                    }
                    return null;
                }
            };
            forecastTask.setOnSucceeded(e -> {
                Parent root;
                try {
                    root = new FXMLLoader(App.class.getResource("chart-view.fxml")).load();
                    Font.loadFont(EvaluateController.class.getResource("/fonts/Inter.ttf").toExternalForm(), 24);
                    root.getStylesheets().add(App.class.getResource("/styles/chartpopup.css").toExternalForm());
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.show();
            });
            new Thread(forecastTask).start();
        });

    }
    private void setDataRangeParams(ApiParameters params) {
        switch (dateRangeModel.dataRangeType) {
            case Forecast -> {
                params.add("past_days", dateRangeModel.forecastPastDays);
                params.add("forecast_days", dateRangeModel.forecastFutureDays);
            }
            case Historic -> {
                params.add("start_date", dateRangeModel.historicStartDate.toString());
                params.add("end_date", dateRangeModel.historicEndDate.toString());
            }
        }
    }
    private Boolean setFilterParams(ApiParameters params) {
        params.add("hourly", "");
        for(FilterOption opt : filterModel.filters) {
            if(!opt.isSet) {
                continue;
            }
            String val = params.get("hourly") + (params.get("hourly").isEmpty() ? "" : ",") + opt.value;
            params.add("hourly", val);
        }
        String currentTemp = switch(Unit.get(prefs.get("unit_temperature", Unit.Celsius.toString()))){
            case Unit.Fahrenheit -> "fahrenheit";
            default -> "celsius";
        };
        String currentPrec = switch (Unit.get(prefs.get("unit_precipation", Unit.Millimeters.toString()))) {
            case Unit.Inches -> "inch";
            default -> "mm";
        };
        String currentSpeed = switch (Unit.get(prefs.get("unit_speed", Unit.KmPerHour.toString()))) {
            case Unit.MPerSecond -> "ms";
            case Unit.Knots -> "kn";
            case Unit.MilesPerHour -> "mph";
            default -> "kmh";
        };
        if (searchModel.currentSearchType == SearchController.SearchType.City
                && searchModel.areCoordsEmpty) {
            if(searchModel.isLocationEmpty) {
                System.out.println("Uzupełnij adres");
                return false;
            }
        } else if (searchModel.areCoordsEmpty) {
            System.out.println("Uzupełnij współrzędne");
            return false;
        }
        params.add("wind_speed_unit", currentSpeed);
        params.add("temperature_unit", currentTemp);
        params.add("precipitation_unit", currentPrec);
        return true;
    }
}
