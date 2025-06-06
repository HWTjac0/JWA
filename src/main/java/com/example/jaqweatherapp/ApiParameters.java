package com.example.jaqweatherapp;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class ApiParameters {
    private Map<String, String> parameters = new HashMap<>();
    public ApiParameters() {}
    public Map<String, String> getParameters() {
        return parameters;
    }
    public void clear() {
        parameters.clear();
    }
    public String get(String key) {
        return parameters.get(key);
    }
    public void add(String key, String value) {
        this.parameters.put(key, value);
    }
    public String getHash() {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            StringBuilder hash = new StringBuilder();
            for (Map.Entry<String, String> entry : this.parameters.entrySet()) {
                hash.append(entry.getKey()).append(":").append(entry.getValue());
            }
            byte[] hashBytes = digest.digest(hash.toString().getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hashBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
    public String getUniqueName() {
        return getHash() + ".json";
    }
}
