package com.example.jaqweatherapp;


import com.fasterxml.jackson.annotation.JsonValue;

public enum CacheTTL {
    Hour(3600),
    Infinite(0);

    private final int ttl;
    CacheTTL(int i) {
        ttl = i;
    }

    @JsonValue
    public int toSeconds() {
        return ttl;
    }
}
