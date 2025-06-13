package com.example.jaqweatherapp;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.util.StringConverter;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.text.NumberFormat;
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
    @FXML private Label locationName;
    @FXML private Label locationCoords;
    @FXML private VBox filterList;
    @FXML private VBox exportList;
    @FXML private Button exportButton;
    @FXML private TextField exportFilename;
    @FXML private Label exportFeedback;

    @FXML private Label chartAddInfoMax;
    @FXML private Label chartAddInfoMin;
    @FXML private Label chartAddInfoAvg;
    @FXML private Label chartAddInfoSlope;
    DateTimeFormatter exportDefaultFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");

    List<CheckBox> exportCheckBoxes = new ArrayList<>();

    enum ExportStatus {
        Success(Color.GREEN, "Eksportowano pomyślnie!"),
        Failure_File_Exists(Color.RED, "Plik o takiej nazwie już istnieje!"),
        Failure_Data_Empty(Color.RED,"Musisz wybrać dane do eksportowania!"),
        Failure_Unknown(Color.RED, "Eksportowanie zakończone niepowodzeniem!");
        private final Color color;
        private final String message;
        ExportStatus(Color color, String message) {
            this.color = color;
            this.message = message;
        }
        public Color getColor() { return color; }
        public String getMessage() { return message; }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        forecastModel = new ForecastModel(Context.getInstance().getForecastModel());
        Context.getInstance().getForecastModel().clear();
        initLocation();
        initFilterList();
        initExportList();
        initExportButton();
        initExportFilename();
        searchModel.locationName = "";
    }
    private void initExportFilename() {
        String fileName = LocalDateTime.now().format(exportDefaultFormat) + ";" + String.join("_", forecastModel.dataMap.keySet());
        exportFilename.setText(fileName);
    }

    private void initLocation() {
        String l = getDisplayAddress();
        locationName.setText("Dane pogodowe dla: " + (
                l == null ? "Nieokreślona lokalizacja" : l )
        );
        locationCoords.setText("Współrzędne geograficzne: (" +
                searchModel.coordinates.latitude + ", " + searchModel.coordinates.longitude + ")"
                );
    }
    private void setExportFeedback(ExportStatus status) {
        String color = "rgb(" + status.getColor().getRed() + "," + status.getColor().getGreen() + "," + status.getColor().getBlue() + ")";
        exportFeedback.setText(status.getMessage());
        exportFeedback.setTextFill(status.getColor());
    }
    private void initExportButton(){
        exportButton.setOnAction(event -> {
            Path exportPath = Paths.get(prefs.get("export_path", SettingsModel.getDefaultExportPath().toString()));
            List<CheckBox> selectedCheckBoxes = exportCheckBoxes.stream().filter(CheckBox::isSelected).toList();
            if(selectedCheckBoxes.isEmpty()) {
                setExportFeedback(ExportStatus.Failure_Data_Empty);
                return;
            }
            forecastModel.exportDataSet.clear();
            for(CheckBox checkBox : selectedCheckBoxes){
                forecastModel.exportDataSet.add(checkBox.getUserData().toString());
            }
            String fileName = LocalDateTime.now().format(exportDefaultFormat) + String.join("_", forecastModel.dataMap.keySet());
            if(!exportFilename.getText().trim().isEmpty()) {
               fileName = exportFilename.getText().trim();
            }
            fileName += ".json";
            Path exportFilePath = Paths.get(exportPath.toString(), fileName);
            if(Files.exists(exportFilePath)) {
                setExportFeedback(ExportStatus.Failure_File_Exists);
                return;
            }
            File file = new File(exportFilePath.toUri());
            ObjectMapper mapper = new ObjectMapper();
            try {
                mapper.writeValue(file, forecastModel);
                setExportFeedback(ExportStatus.Success);
            } catch (IOException e) {
                setExportFeedback(ExportStatus.Failure_Unknown);
            }

        });
    }
    private String getDisplayAddress() {
        if(!searchModel.locationName.isEmpty()) {
            return searchModel.locationName;
        } else {
            try {
                return geocodingApiClient.reverseGeocode(searchModel.coordinates.latitude, searchModel.coordinates.longitude).join().displayName;
            } catch (Exception e) {
                return "Nie udało sie uzyskać adresu";
            }
        }
    }
    private void initExportList() {
        for(String filter : forecastModel.dataMap.keySet()) {
            CheckBox exportCheckbox = new CheckBox(FilterModel.getFilterDisplay(filter));
            exportCheckbox.setUserData(filter);
            exportCheckbox.setMaxWidth(Double.MAX_VALUE);
            exportCheckbox.setSelected(true);
            exportList.getChildren().add(exportCheckbox);
            exportCheckBoxes.add(exportCheckbox);
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
                buildChartAddtionalInfo(filterButton.getUserData().toString());
            });

            filterList.getChildren().add(filterButton);
        }
        filterGroup.getToggles().getFirst().setSelected(true);
        buildChart(filterGroup.getSelectedToggle().getUserData().toString());
        buildChartAddtionalInfo(filterGroup.getSelectedToggle().getUserData().toString());
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
        // MAIN DATA
        XYChart.Series<Number, Number> series = new XYChart.Series();
        for (int i = 0; i < forecastModel.dateSeries.size(); i++) {
            series.getData().add(new XYChart.Data<>(dates.get(i), data.get(i)));
        }
        chart.getData().add(series);
        // TREND DATA
        XYChart.Series<Number, Number> trendSeries = new XYChart.Series<>();
        RegressionResult trend = forecastModel.statsMap.get(currentFilter).regressionResult();
        for(int i = 0; i < forecastModel.dateSeries.size(); i++) {
           trendSeries.getData().add(new XYChart.Data<>(dates.get(i), trend.intercept() + dates.get(i) * trend.slope()));
        }
        chart.getData().add(trendSeries);
        chart.setLegendVisible(false);
        chart.setPrefWidth(Region.USE_COMPUTED_SIZE);
        chart.setPrefHeight(Region.USE_COMPUTED_SIZE);
        HBox.setHgrow(chart, Priority.ALWAYS);
        chartContainer.getChildren().add(chart);
    }
    private void buildChartAddtionalInfo(String currentFilter) {
        Stats stats = forecastModel.statsMap.get(currentFilter);
        String unit = forecastModel.dataMap.get(currentFilter).unit().replace("\"", "");
        NumberFormat numFormatter = new DecimalFormat("#0.00");
        chartAddInfoMax.setText(stats.maxValue().getValue() + unit + ", " + stats.maxValue().getKey());
        chartAddInfoMin.setText(stats.minValue().getValue() + unit + ", " + stats.minValue().getKey());
        chartAddInfoAvg.setText(numFormatter.format(stats.average()) + unit);

        Color trendColor = Color.DARKVIOLET;
        String trend = "Nieokreślony";
        Double slope =stats.regressionResult().slope();
        if(slope > 0) {
            trendColor = Color.GREEN;
            trend = "Rosnący";
        } else if(slope < 0) {
            trendColor = Color.RED;
            trend = "Malejący";
        }
        chartAddInfoSlope.setText(trend);
        chartAddInfoSlope.setTextFill(trendColor);
    }
}
