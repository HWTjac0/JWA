package com.example.jaqweatherapp;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.*;

@JsonDeserialize(using = ForecastModelDeserializer.class)
@JsonSerialize(using = ForecastModelSerializer.class)
public class ForecastModel {
    public HashMap<String, DataSeries> dataMap = new HashMap<>();
    public List<Long> dateSeries = new ArrayList<Long>();
    @JsonIgnore
    public Set<String> exportDataSet  = new HashSet<>();
    @JsonIgnore
    public String displayAddress;
    @JsonIgnore
    public ForecastModel() {}
    @JsonIgnore
    public ForecastModel(ForecastModel model) {
        this(model.dataMap, model.dateSeries, model.displayAddress);
    }
    @JsonIgnore
    public ForecastModel(HashMap<String, DataSeries> dataMap, List<Long> dateSeries, String displayAddress) {
        this.dataMap = (HashMap<String, DataSeries>) dataMap.clone();
        this.dateSeries = new ArrayList<>(dateSeries);
        this.displayAddress = displayAddress == null ? null : new String(displayAddress);
    }
}
