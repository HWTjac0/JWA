package com.example.jaqweatherapp;

import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.chart.Axis;
import javafx.scene.chart.LineChart;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MultilineChart extends StackPane {
    private  ObservableList<LineChart> charts = FXCollections.observableArrayList();
    private Map<LineChart, Color> chartColorMap = new HashMap<LineChart, Color>();
    private  LineChart baseChart;
    public MultilineChart(LineChart baseChart) {
        this.baseChart = baseChart;
        this.baseChart.setCreateSymbols(false);
        this.baseChart.setLegendVisible(false);
        this.baseChart.getXAxis().setAnimated(false);
        this.baseChart.getYAxis().setAnimated(false);
    }
    public void addChart(LineChart chart) {
        styleChart(chart);
        charts.add(chart);
        buildChart();
    }
    public void addCharts(ArrayList<LineChart> charts) {
        System.out.println(charts.size());
        for (LineChart chart : charts) {
            styleChart(chart);
            this.charts.add(chart);
        }
        buildChart();
    }
    private void styleChart(LineChart chart) {
        Node contentBackground = chart.lookup(".chart-content").lookup(".chart-plot-background");
        contentBackground.setStyle("-fx-background-color: transparent;");
        chart.setVerticalZeroLineVisible(false);
        chart.setHorizontalZeroLineVisible(false);
        chart.setVerticalGridLinesVisible(false);
        chart.setHorizontalGridLinesVisible(false);
        chart.setCreateSymbols(false);
        chart.getXAxis().setVisible(true);
    }

    public void buildChart() {
        getChildren().clear();
        getChildren().add(baseChart);
        for(LineChart chart : charts) {
            getChildren().add(chart);
        }
    }

}
