package com.example.jaqweatherapp;

public class ComboOption {
    private String displayName;
    private String value;
    public ComboOption(String displayName, String value) {
        this.displayName = displayName;
        this.value = value;
    }
    public String getDisplayName() { return displayName; }
    public String getValue() { return value; }
    @Override
    public String toString() { return displayName; }
}
