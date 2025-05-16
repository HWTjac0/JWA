package com.example.jaqweatherapp;

import com.almasb.fxgl.core.collection.Array;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class EvaluateController implements Initializable {
    @FXML Button fetchButton;
    DataSearchModel dataSearchModel = Context.getInstance().getDataSearchModel();
    WeatherApiClient weatherApiClient = new WeatherApiClient();
    public void initialize(URL location, ResourceBundle resources) {
        fetchButton.setOnAction(event -> {
            HashMap<String, String> params = new HashMap<>();
            params.put("hourly", "");
            for(FilterOption opt : dataSearchModel.filters) {
                if(!opt.isSet) {
                    continue;
                }
                String val = params.get("hourly") + (params.get("hourly").isEmpty() ? "" : ",") + opt.value;
                params.put("hourly", val);
            }
            Map<UnitType, Unit> currentUnits = dataSearchModel.unitManager.currentUnits;
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
            params.put("wind_speed_unit", currentSpeed);
            params.put("temperature_unit", currentTemp);
            params.put("precipitation_unit", currentPrec);
            System.out.println(weatherApiClient.buildQueryString(params));
        });
    }
}
