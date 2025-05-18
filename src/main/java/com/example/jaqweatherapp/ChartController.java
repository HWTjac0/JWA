package com.example.jaqweatherapp;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.ResourceBundle;

public class ChartController implements Initializable {
    ForecastModel forecastModel = Context.getInstance().getForecastModel();
    @FXML private HBox chartContainer;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Data");
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Wartość");
        LineChart<String, Number> lineChart = new LineChart<>(xAxis, yAxis);
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        for(int i = 0; i < forecastModel.dateSeries.size(); i++) {
            series.getData()
                    .add(new XYChart.Data<String, Number>(
                            forecastModel.dateSeries.get(i),
                            forecastModel.dataMap.get("temperature_2m").data().get(i)
                    ));
        }
        lineChart.getData().add(series);
        chartContainer.getChildren().add(lineChart);
    }
}
