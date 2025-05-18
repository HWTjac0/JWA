package com.example.jaqweatherapp;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import java.net.URL;
import java.util.*;

public class EvaluateController implements Initializable {
    @FXML Button fetchButton;
    FilterModel filterModel = Context.getInstance().getFilterModel();
    SearchModel searchModel = Context.getInstance().getSearchModel();
    WeatherApiClient weatherApiClient = Context.getInstance().getWeatherApiClient();

    public void initialize(URL location, ResourceBundle resources) {
        fetchButton.setOnAction(event -> {
            HashMap<String, String> params = new HashMap<>();
            if(!setFilterParams(params)) return;
            params.put("latitude", String.valueOf(searchModel.coordinates.latitude));
            params.put("longitude", String.valueOf(searchModel.coordinates.longitude));
            ObjectMapper mapper = new ObjectMapper();
                    weatherApiClient.getForecast(params)
                    .thenApply(reply -> {
                        try {
                            ForecastModel forecastModel = mapper.readValue(reply, ForecastModel.class);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                        return reply;
                    });
        });
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
