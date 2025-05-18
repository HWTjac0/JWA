package com.example.jaqweatherapp;

import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class ChartController implements Initializable {
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println(Context.getInstance().getForecastModel().dateSeries);
    }
}
