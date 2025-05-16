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

        });
    }
}
