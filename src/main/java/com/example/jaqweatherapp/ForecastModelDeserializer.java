package com.example.jaqweatherapp;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.List;
import java.util.stream.StreamSupport;

public class ForecastModelDeserializer extends StdDeserializer<ForecastModel> {
    public ForecastModelDeserializer() {
        this(null);
    }
    public ForecastModelDeserializer(Class<ForecastModel> t) {
        super(t);
    }

    @Override
    public ForecastModel deserialize(JsonParser jp, DeserializationContext ctxt)
            throws IOException, JsonProcessingException {
        ForecastModel forecastModel = Context.getInstance().getForecastModel();
        FilterModel filterModel = Context.getInstance().getFilterModel();
        JsonNode root = jp.getCodec().readTree(jp);
        JsonNode data = root.get("hourly");
        JsonNode units = root.get("hourly_units");
        forecastModel.dateSeries.addAll(StreamSupport.stream(data.get("time").spliterator(), false)
                .map((node) -> {
                    String dateString = node.asText().replaceAll("\"", "");
                    return LocalDateTime
                            .parse(dateString, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                            .toInstant(ZoneOffset.UTC)
                            .toEpochMilli();
                })
                .toList());
        Iterator<String> fields = data.fieldNames();

        fields.forEachRemaining(field -> {
            if(field == "time") {
                return;
            }
            List<Double> values = StreamSupport.stream(data.get(field).spliterator(), false)
                    .map(JsonNode::asDouble)
                    .toList();
            DataSeries series = new DataSeries(values, units.get(field).toString());
            forecastModel.dataMap.put(field, series);
        });
        return forecastModel;
    }
}
