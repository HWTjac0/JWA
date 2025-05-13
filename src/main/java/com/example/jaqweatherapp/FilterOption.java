package com.example.jaqweatherapp;

public class FilterOption {
    public String displayName;
    public String value;
    public UnitType unit;
    public Boolean isSet;
    public FilterOption(String displayName, String value, UnitType unit) {
        this.displayName = displayName;
        this.value = value;
        this.unit = unit;
        this.isSet = false;
    }
}
