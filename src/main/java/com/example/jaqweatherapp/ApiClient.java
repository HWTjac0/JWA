package com.example.jaqweatherapp;

import java.net.http.HttpClient;

public abstract class ApiClient {
    protected final HttpClient httpClient;
    protected final String baseUrl;
    protected ApiClient(String baseUrl) {
        this.baseUrl = baseUrl;
        this.httpClient = buildHttpClient();
    }
    protected HttpClient buildHttpClient() {
        return HttpClient
                .newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .followRedirects(HttpClient.Redirect.NORMAL)
                .build();
    }
}
