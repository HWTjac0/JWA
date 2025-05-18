package com.example.jaqweatherapp;

import java.awt.*;
import java.util.List;

public record DataSeries (List<Double> data, String unit) {}
