package com.example.jaqweatherapp;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.prefs.Preferences;

public class SettingsModel {
    Preferences prefs = Preferences.userRoot().node("/com/hwtjac0/JaqWeatherApp");
    UnitManager unitManager = new UnitManager();
    Path defaultExportPath;

    public SettingsModel() {
        setDefaults();
    }
    public void setDefaults() {
        unitManager.currentUnits.put(UnitType.SPEED, Unit.get(prefs.get("unit_speed", Unit.KmPerHour.toString())));
        unitManager.currentUnits.put(UnitType.TEMPERATURE, Unit.get(prefs.get("unit_temperature", Unit.Celsius.toString())));
        unitManager.currentUnits.put(UnitType.PRECIPITATION, Unit.get(prefs.get("unit_precipation", Unit.Millimeters.toString())));
        defaultExportPath =Paths.get(prefs.get("export_path", getDefaultExportPath().toString()));
    }
    public void applySettings() {
        prefs.put("unit_speed", unitManager.currentUnits.get(UnitType.SPEED).toString());
        prefs.put("unit_temperature", unitManager.currentUnits.get(UnitType.TEMPERATURE).toString());
        prefs.put("unit_precipation", unitManager.currentUnits.get(UnitType.PRECIPITATION).toString());
        prefs.put("export_path", defaultExportPath.toString());
    }
    public static Path getDefaultExportPath() {
        String os = System.getProperty("os.name").toLowerCase();
        Path exportDir = null;
        if (os.contains("win")) {
            String docs = Paths.get(System.getProperty("user.home"), "Documents").toString();
            exportDir = Paths.get(docs, "JaqWeatherApp", "exports");
        } else {
            exportDir = Paths.get(System.getProperty("user.home"), "Documents", "JaqWeatherApp", "exports");
        }
        if(!Files.exists(exportDir)) {
            try {
                Files.createDirectories(exportDir);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        return exportDir;
    }
}
