package com.example.jaqweatherapp;


import java.time.LocalDate;

public class DateRangeModel {
    // For given max days into the future or past index from 0 so you dont include current day
    public final int MAX_PAST_DAYS = 89;
    public final int MAX_FUTURE_DAYS = 15;
    public enum DataRangeType { Forecast, Historic };
    public DataRangeType dataRangeType = DataRangeType.Forecast;
    public LocalDate historicStartDate = LocalDate.now().minusDays(1);
    public LocalDate historicEndDate = LocalDate.now();
    public String forecastPastDays = "0";
    public String forecastFutureDays = "1";
}
