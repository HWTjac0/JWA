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
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class EvaluateController implements Initializable {
    @FXML Button fetchButton;
    FilterModel filterModel = Context.getInstance().getFilterModel();
    SearchModel searchModel = Context.getInstance().getSearchModel();
    DateRangeModel dateRangeModel = Context.getInstance().getDateRangeModel();
    WeatherApiClient weatherApiClient = Context.getInstance().getWeatherApiClient();
    ForecastModel forecastModel = Context.getInstance().getForecastModel();

    public void initialize(URL location, ResourceBundle resources) {
        fetchButton.setOnAction(event -> {
            HashMap<String, String> params = new HashMap<>();
            if(!setFilterParams(params)) return;
            setDataRangeParams(params);
            params.put("latitude", String.valueOf(searchModel.coordinates.latitude));
            params.put("longitude", String.valueOf(searchModel.coordinates.longitude));
            ObjectMapper mapper = new ObjectMapper();
            Task<ForecastModel> forecastTask = new Task<>() {
                @Override
                protected ForecastModel call() throws Exception {
                    try{
                        forecastModel.dateSeries.clear();
                        forecastModel.dataMap.clear();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    CompletableFuture<String> modelPromise = weatherApiClient.getForecast(params);
                    String response = modelPromise.join();
                    try {
                        return mapper.readValue(response, ForecastModel.class);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return null;
                }
            };
            forecastTask.setOnSucceeded(e -> {
                Parent root;
                try {
                    root = new FXMLLoader(App.class.getResource("chart-view.fxml")).load();
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
    private void setDataRangeParams(HashMap<String, String> params) {
        switch (dateRangeModel.dataRangeType) {
            case Forecast -> {
                params.put("past_days", dateRangeModel.forecastPastDays);
                params.put("future_days", dateRangeModel.forecastFutureDays);
            }
            case Historic -> {
                params.put("start_date", dateRangeModel.historicStartDate.toString());
                params.put("end_date", dateRangeModel.historicEndDate.toString());
            }
        }
    }
    private Boolean setFilterParams(HashMap<String, String> params) {
        params.put("hourly", "");
        for(FilterOption opt : filterModel.filters) {
            if(!opt.isSet) {
                continue;
            }
            String val = params.get("hourly") + (params.get("hourly").isEmpty() ? "" : ",") + opt.value;
            params.put("hourly", val);
        }
        Map<UnitType, Unit> currentUnits = filterModel.unitManager.currentUnits;
        String currentTemp = switch(currentUnits.get(UnitType.TEMPERATURE)) {
            case Unit.Fahrenheit -> "fahrenheit";
            default -> "celsius";
        };
        String currentPrec = switch (currentUnits.get(UnitType.PRECIPITATION)) {
            case Unit.Inches -> "inch";
            default -> "mm";
        };
        String currentSpeed = switch (currentUnits.get(UnitType.SPEED)) {
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
        params.put("wind_speed_unit", currentSpeed);
        params.put("temperature_unit", currentTemp);
        params.put("precipitation_unit", currentPrec);
        return true;
    }
}
