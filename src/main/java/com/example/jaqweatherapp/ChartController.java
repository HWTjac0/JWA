package com.example.jaqweatherapp;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.DirectoryChooser;
import javafx.util.StringConverter;

import java.io.File;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Timestamp;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.prefs.Preferences;

public class ChartController implements Initializable {
    ForecastModel forecastModel;
    SearchModel searchModel = Context.getInstance().getSearchModel();
    GeocodingApiClient geocodingApiClient = Context.getInstance().getGeocodingApiClient();
    Preferences prefs = Preferences.userRoot().node("/com/hwtjac0/JaqWeatherApp");

    @FXML private HBox chartContainer;
    @FXML private Label chartTitle;
    @FXML private VBox filterList;
    @FXML private VBox exportList;
    @FXML private Button exportButton;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        forecastModel = new ForecastModel(Context.getInstance().getForecastModel());
        initFilterList();
        initExportList();
        initExportButton();
        chartTitle.setText("Dane pogodowe dla: " + getDisplayAddress());
        searchModel.locationName = "";
    }
    private void initExportButton(){
        DirectoryChooser directoryChooser = new DirectoryChooser();
        exportButton.setOnAction(event -> {
            Path exportPath = Paths.get(prefs.get("export_path", SettingsModel.getDefaultExportPath().toString()));
        });
    }
    private String getDisplayAddress() {
        if(!searchModel.locationName.isEmpty()) {
            return searchModel.locationName;
        } else {
            return geocodingApiClient.reverseGeocode(searchModel.coordinates.latitude, searchModel.coordinates.longitude).join().displayName;
        }
    }
    private void initExportList() {
        for(String filter : forecastModel.dataMap.keySet()) {
            CheckBox exportCheckbox = new CheckBox(FilterModel.getFilterDisplay(filter));
            exportCheckbox.setUserData(filter);
            exportCheckbox.setMaxWidth(Double.MAX_VALUE);
            exportCheckbox.setSelected(true);
            exportList.getChildren().add(exportCheckbox);
        }
    }
    private void initFilterList() {
        ToggleGroup filterGroup = new ToggleGroup();
        for(String filter : forecastModel.dataMap.keySet()) {
            ToggleButton filterButton = new ToggleButton(FilterModel.getFilterDisplay(filter));
            filterButton.setUserData(filter);
            filterButton.setToggleGroup(filterGroup);
            filterButton.setMaxWidth(Double.MAX_VALUE);

            filterButton.onActionProperty().set(event -> {
                buildChart(filterButton.getUserData().toString());
            });

            filterList.getChildren().add(filterButton);
        }
        filterGroup.getToggles().getFirst().setSelected(true);
        buildChart(filterGroup.getSelectedToggle().getUserData().toString());
    }
    private void buildChart(String currentFilter) {
        chartContainer.getChildren().clear();
        List<Double> data = forecastModel.dataMap.get(currentFilter).data();
        List<Long> dates = forecastModel.dateSeries;
        NumberAxis xAxis = new NumberAxis();
        xAxis.setLabel("Data");
        long minDate = dates.getFirst();
        long maxDate = dates.getLast();
        xAxis.setAutoRanging(false);
        xAxis.setAnimated(false);
        xAxis.setLowerBound(minDate);
        xAxis.setUpperBound(maxDate);
        xAxis.setTickUnit((maxDate - minDate) / 10);
        xAxis.setTickLabelFormatter(new StringConverter<Number>() {
            private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:ss");
            @Override
            public String toString(Number object) {
               return dateFormat.format(new Date(object.longValue() ));
            }

            @Override
            public Number fromString(String string) {
                return null;
            }
        });
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Wartość [" + forecastModel.dataMap.get(currentFilter).unit() + "]");
        yAxis.setAutoRanging(false);
        yAxis.setAnimated(false);
        double minVal = data.stream().min(Double::compare).get();
        double maxVal = data.stream().max(Double::compare).get();
        yAxis.setLowerBound(minVal);
        yAxis.setUpperBound(maxVal);

        LineChart<Number, Number> chart = new LineChart<>(xAxis, yAxis);
        chart.setCreateSymbols(false);
        XYChart.Series<Number, Number> series = new XYChart.Series();
        for (int i = 0; i < forecastModel.dateSeries.size(); i++) {
            series.getData().add(new XYChart.Data<>(dates.get(i), data.get(i)));
        }
        chart.getData().add(series);
        chart.setLegendVisible(false);
        chart.setPrefWidth(Region.USE_COMPUTED_SIZE);
        chart.setPrefHeight(Region.USE_COMPUTED_SIZE);
        HBox.setHgrow(chart, Priority.ALWAYS);
        chartContainer.getChildren().add(chart);
    }
}
