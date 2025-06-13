package com.example.jaqweatherapp;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class ForecastModelSerializer extends StdSerializer<ForecastModel> {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
    public ForecastModelSerializer() {
        super(ForecastModel.class);
    }
    @Override
    public void serialize(ForecastModel forecastModel, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeStartObject();
        gen.writeNumberField("longitude", forecastModel.longitude);
        gen.writeNumberField("latitude", forecastModel.latitude);
        gen.writeObjectFieldStart("hourly_units");
        gen.writeStringField("time", "iso8601");
        for(String v : forecastModel.exportDataSet) {
            gen.writeStringField(v, forecastModel.dataMap.get(v).unit().replace("\"", ""));
        }
        gen.writeEndObject();

        gen.writeObjectFieldStart("hourly");
        gen.writeArrayFieldStart("time");
        for(Long date : forecastModel.dateSeries) {
            String fDate = LocalDateTime.ofInstant(Instant.ofEpochMilli(date), ZoneId.of("UTC")).format(formatter);
            gen.writeString(fDate);
        }
        gen.writeEndArray();
        for(String k : forecastModel.exportDataSet) {
            gen.writeArrayFieldStart(k);
            for(Double v : forecastModel.dataMap.get(k).data()) {
                gen.writeNumber(v);
            }
            gen.writeEndArray();
        }
        gen.writeEndObject();
    }
}
