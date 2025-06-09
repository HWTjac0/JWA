package com.example.jaqweatherapp;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.Timestamp;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Set;

public class ChartController implements Initializable {
    ForecastModel forecastModel = Context.getInstance().getForecastModel();
    @FXML private HBox chartContainer;
    @FXML private VBox filterList;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initFilterList();
    }
    private void initFilterList() {
        ToggleGroup filterGroup = new ToggleGroup();
        for(String filter : forecastModel.dataMap.keySet()) {
            ToggleButton filterButton = new ToggleButton(FilterModel.getFilterDisplay(filter));
            filterButton.setUserData(filter);
            filterButton.setToggleGroup(filterGroup);
            filterButton.setMaxWidth(Double.MAX_VALUE);

            filterButton.onActionProperty().set(event -> {
                System.out.println(filterButton.getUserData());
            });

            filterList.getChildren().add(filterButton);
        }
        filterGroup.getToggles().getFirst().setSelected(true);
    }
    private void buildChart(String currentFilter) {
        CategoryAxis xAxis = new CategoryAxis();
    }
}
