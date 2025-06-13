package com.example.jaqweatherapp;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;

public class CacheUnit {
    @JsonProperty
    private long cacheTime;
    @JsonProperty
    private CacheTTL ttl;
    @JsonProperty
    private String data;
    @JsonProperty
    private String query;
    public CacheUnit() {}
    public CacheUnit(long cacheTime, CacheTTL ttl, String data, String query) {
        this.cacheTime = cacheTime;
        this.ttl = ttl;
        this.data = data;
        this.query = query;
    }
    public void setQuery(String query) { this.query = query; }
    public void setCacheTime(long cacheTime) { this.cacheTime = cacheTime; }
    public void setTtl(CacheTTL ttl) { this.ttl = ttl; }
    public void setData(String data) { this.data = data; }
    public long getCacheTime() {
        return cacheTime;
    }
    public CacheTTL getTtl() {
        return ttl;
    }
    public String getData() {
        return data;
    }
    public String getQuery() {
        return query;
    }
    @JsonIgnore
    public boolean isExpired() {
        return !(ttl == CacheTTL.Infinite) || Instant.now().getEpochSecond() - cacheTime > ttl.toSeconds();
    }
}
