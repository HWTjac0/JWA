package com.example.jaqweatherapp;

public record Result<T>(boolean valid, T data) {}
