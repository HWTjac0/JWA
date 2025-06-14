package com.example.jaqweatherapp;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import javafx.util.Pair;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

record Stats(Pair<String, Double> maxValue, Pair<String, Double> minValue, Double average, RegressionResult regressionResult) {
}
record RegressionResult(double slope, double intercept) {}
@JsonDeserialize(using = ForecastModelDeserializer.class)
@JsonSerialize(using = ForecastModelSerializer.class)
public class ForecastModel {
    public HashMap<String, DataSeries> dataMap = new HashMap<>();
    public List<Long> dateSeries = new ArrayList<Long>();
    public Double latitude = null;
    public Double longitude = null;
    @JsonIgnore
    public HashMap<String, Stats> statsMap = new HashMap<>();
    @JsonIgnore
    public Set<String> exportDataSet = new HashSet<>();
    @JsonIgnore
    public String displayAddress = "";

    @JsonIgnore
    public ForecastModel() {
    }

    @JsonIgnore
    public ForecastModel(ForecastModel model) {
        this(model.dataMap, model.dateSeries, model.displayAddress);
    }

    @JsonIgnore
    public ForecastModel(HashMap<String, DataSeries> dataMap, List<Long> dateSeries, String displayAddress) {
        this.dataMap = (HashMap<String, DataSeries>) dataMap.clone();
        this.dateSeries = new ArrayList<>(dateSeries);
        this.displayAddress = displayAddress == null ? null : new String(displayAddress);
        populateStats();
    }

    @JsonIgnore
    private void populateStats() {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        for (String key : dataMap.keySet()) {
            double max = 0;
            int maxIndex = 0;
            double min = Double.MAX_VALUE;
            int minIndex = 0;
            double sum = 0;
            List<Double> values = dataMap.get(key).data();
            for (int i = 0; i < dateSeries.size(); i++) {
                if (values.get(i) > max) {
                    max = values.get(i);
                    maxIndex = i;
                }
                if (values.get(i) < min) {
                    min = values.get(i);
                    minIndex = i;
                }
                sum += values.get(i);
            }
            Pair<String, Double> maxValue = new Pair<>(
                    LocalDateTime.ofInstant(Instant.ofEpochMilli(dateSeries.get(maxIndex)), ZoneId.of("UTC")).format(formatter),
                    max);
            Pair<String, Double> minValue = new Pair<>(
                    LocalDateTime.ofInstant(Instant.ofEpochMilli(dateSeries.get(minIndex)), ZoneId.of("UTC")).format(formatter),
                    min);
            double average = sum / dateSeries.size();
            RegressionResult rres = calculateTrend(average, dataMap.get(key).data());
            Stats stats = new Stats(maxValue, minValue, average, rres);
            statsMap.put(key, stats);
        }
    }
    @JsonIgnore
    private RegressionResult calculateTrend(double avgY, List<Double> yValues) {
        double sumX = 0;
        for (Long xValue : dateSeries) { // where xValues is your dateSeries
            sumX += xValue;
        }
        double avgX = sumX / dateSeries.size();

        double beta;
        double betaNumerator = 0;
        double betaDenominator = 0;
        for(int i = 0; i < dateSeries.size(); i++) {
            betaNumerator += (dateSeries.get(i) - avgX) * (yValues.get(i) - avgY);
            betaDenominator += Math.pow(dateSeries.get(i) - avgX, 2);
        }
        beta = betaNumerator / betaDenominator;
        double alpha = avgY - (beta * avgX);
        return new RegressionResult(beta, alpha);
    }
    @JsonIgnore
    public void clear() {
        dataMap.clear();
        dateSeries.clear();
        statsMap.clear();
        exportDataSet.clear();
    }
}
