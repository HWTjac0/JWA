package com.example.jaqweatherapp;


import java.time.LocalDate;
import java.util.Date;

public class DateRangeModel {
    public enum DataRangeType { Forecast, Historic };
    public DataRangeType dataRangeType = DataRangeType.Forecast;
    public LocalDate historicStartDate = LocalDate.now().minusDays(1);
    public LocalDate historicEndDate = LocalDate.now();
    public String forecastPastDays = "0";
    public String forecastFutureDays = "1";
}
