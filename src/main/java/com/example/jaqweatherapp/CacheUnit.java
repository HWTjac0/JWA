package com.example.jaqweatherapp;


import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;

public class CacheUnit {
    @JsonProperty
    private long cacheTime;
    @JsonProperty
    private int ttl;
    @JsonProperty
    private String data;
    public CacheUnit() {}
    public CacheUnit(long cacheTime, int ttl, String data) {
        this.cacheTime = cacheTime;
        this.ttl = ttl;
        this.data = data;
    }
    public void setCacheTime(long cacheTime) { this.cacheTime = cacheTime; }
    public void setTtl(int ttl) { this.ttl = ttl; }
    public void setData(String data) { this.data = data; }
    public long getCacheTime() {
        return cacheTime;
    }
    public int getTtl() {
        return ttl;
    }
    public String getData() {
        return data;
    }
    public boolean isExpired() {
        return Instant.now().getEpochSecond() - cacheTime > ttl;
    }
}
