package com.example.jaqweatherapp;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

public class CacheManager {
    Map<String, Path> availableCache = new HashMap<>();
    Path cacheDir;
    ObjectMapper mapper = new ObjectMapper();
    public CacheManager() {
        cacheDir = getUserCacheDir();
        availableCache = readAvailableCache();
    }
    private CacheUnit readCache(Path cachePath) {
        try {
            return mapper.readValue(cachePath.toFile(), CacheUnit.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void invalidateCache(String hash) {
        deleteCache(availableCache.get(hash));
        availableCache.remove(hash);
    }
    public void deleteCache(Path cachePath) {
        cacheDir.toFile().delete();
    }
    public Map<String, Path> readAvailableCache() {
        Map<String, Path> cache = new HashMap<>();
        for(File file : cacheDir.toFile().listFiles()) {
            String cacheName =file.getName().substring(0, file.getName().lastIndexOf("."));
            CacheUnit c = readCache(file.toPath());
            if(c.isExpired()) {
                file.delete();
            } else {
                cache.put(cacheName, file.toPath());
            }
        }
        return cache;
    }
    public Result<String> getCache(String hash) {
        if(!availableCache.containsKey(hash)) {
            return new Result<>(false, "");
        }
        CacheUnit cacheUnit = readCache(availableCache.get(hash));
        if(cacheUnit.isExpired()) {
            invalidateCache(hash);
            return new Result<>(false, "");
        }
        return new Result<>(true, cacheUnit.getData());
    }
    public void setCache(String hash, String data, CacheTTL expiration, String query) {
        CacheUnit cacheUnit = new CacheUnit(Instant.now().getEpochSecond(), expiration, data, query);
        String filename = hash + ".json";
        Path currentCachePath = Paths.get(cacheDir.toString(), filename);
        availableCache.put(hash, currentCachePath);
        try {
            mapper.writeValue(currentCachePath.toFile(), cacheUnit);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
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
