package com.example.jaqweatherapp;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Set;

public class ChartController implements Initializable {
    ForecastModel forecastModel = Context.getInstance().getForecastModel();
    @FXML private HBox chartContainer;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Data");
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Wartość");
        LineChart baseChart = new LineChart<>(xAxis, yAxis);
        baseChart.getYAxis().setDisable(true);
        MultilineChart multilineChart = new MultilineChart(baseChart);

        Set<String> filters = forecastModel.dataMap.keySet();
        filters.remove("time");
        ArrayList<LineChart> charts = new ArrayList<>();
        for(String filter : filters) {
            LineChart<String, Number> lineChart = new LineChart<>(xAxis, yAxis);
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            System.out.println("Filter: " + filter);
            for(int i = 0; i < forecastModel.dateSeries.size(); i++) {
                series.getData()
                        .add(new XYChart.Data<String, Number>(
                                forecastModel.dateSeries.get(i),
                                forecastModel.dataMap.get(filter).data().get(i)
                        ));
            }
            lineChart.getData().add(series);
            charts.add(lineChart);
        }
        multilineChart.addCharts(charts);
        chartContainer.getChildren().add(multilineChart);
        HBox.setHgrow(multilineChart, Priority.ALWAYS);
    }
}
