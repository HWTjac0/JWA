package com.example.jaqweatherapp;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class CacheManager {
    List<String> availableCache = new ArrayList<>();
    String cacheDir;
    public CacheManager() {
        cacheDir = getUserCacheDir().toString();
        System.out.println("Cache dir: " + cacheDir);
    }
    public void readAvailableCache() {

    }
    public static Path getUserCacheDir() {
        String os = System.getProperty("os.name").toLowerCase();
        Path cacheDir = null;
        if (os.contains("win")) {
            String appdata = System.getenv("LOCALAPPDATA");
            if(appdata == null) {
                appdata = Paths.get(System.getProperty("user.home"), "AppData" , "Local").toString();
            }
            cacheDir = Paths.get(appdata, "JaqWeatherApp", "cache");
        } else if(os.contains("linux")){
            String xdgCache = System.getenv("XDG_CACHE_HOME");
            if(xdgCache != null && !xdgCache.isEmpty()) {
                cacheDir = Paths.get(xdgCache, "JaqWeatherApp");
            } else {
                cacheDir = Paths.get(System.getProperty("user.home"), ".cache", "JaqWeatherApp");
            }
        }
        if(!Files.exists(cacheDir)) {
            try {
                Files.createDirectories(cacheDir);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        return cacheDir;
    }
}
