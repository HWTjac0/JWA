package com.example.jaqweatherapp;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.Set;

public class ChartController implements Initializable {
    ForecastModel forecastModel = Context.getInstance().getForecastModel();
    @FXML private StackPane chartContainer;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Set<String> filters = forecastModel.dataMap.keySet();
        filters.remove("time");

        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Data");
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Wartość");
        LineChart<String, Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setCreateSymbols(false);

        for(String filter : filters) {
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            for(int i = 0; i < forecastModel.dateSeries.size(); i++) {
                series.getData()
                        .add(new XYChart.Data<String, Number>(
                                forecastModel.dateSeries.get(i),
                                forecastModel.dataMap.get(filter).data().get(i)
                        ));
            }
            lineChart.getData().add(series);
        }
        chartContainer.getChildren().add(lineChart);
    }
}
