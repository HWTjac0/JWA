package com.example.jaqweatherapp;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

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
    protected HttpRequest.Builder buildRequest(String endpoint) {
        return HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + endpoint));
    }
    protected String buildQueryString(Map<String, String> parameters) {
        if (parameters == null || parameters.isEmpty()) {
            return "";
        }
        String params = parameters.entrySet()
                .stream()
                .map(e -> e.getKey() + "=" + e.getValue())
                .collect(Collectors.joining("&"));
        return "?" + params;
    }
    protected CompletableFuture<String> sendGETRequest(String endpoint, Map<String, String> parameters) {
        String queryString = buildQueryString(parameters);
        HttpRequest req = buildRequest(endpoint + queryString)
                .GET()
                .build();
        System.out.println(req.uri().toString());
        return httpClient
                .sendAsync(req, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body);
    }
}
