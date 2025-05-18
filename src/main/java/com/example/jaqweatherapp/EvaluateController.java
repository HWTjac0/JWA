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
                    return;
                }
            } else if (searchModel.areCoordsEmpty) {
                System.out.println("Uzupełnij współrzędne");
                return;
            }
            params.put("wind_speed_unit", currentSpeed);
            params.put("temperature_unit", currentTemp);
            params.put("precipitation_unit", currentPrec);
            params.put("latitude", String.valueOf(searchModel.coordinates.latitude));
            params.put("longitude", String.valueOf(searchModel.coordinates.longitude));
            ObjectMapper mapper = new ObjectMapper();
                    weatherApiClient.getForecast(params)
                    .thenApply(reply -> {
                        try {
                            ForecastModel forecastModel = mapper.readValue(reply, ForecastModel.class);
                            System.out.println(forecastModel.data.keySet());
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                        return reply;
                    });
        });
    }
}
