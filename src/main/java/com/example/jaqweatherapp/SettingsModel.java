package com.example.jaqweatherapp;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class SettingsModel {
    UnitManager unitManager = new UnitManager();
    Path defaultExportPath = getDefaultExportPath();

    public SettingsModel() {}
    public static Path getDefaultExportPath() {
        String os = System.getProperty("os.name").toLowerCase();
        Path exportDir = null;
        if (os.contains("win")) {
            String docs = Paths.get(System.getProperty("user.home"), "Documents").toString();
            exportDir = Paths.get(docs, "JaqWeatherApp", "exports");
        } else if(os.contains("linux")){
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
