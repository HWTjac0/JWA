package com.example.jaqweatherapp;

public class Context {
    private static Context context = new Context();
    private DataSearchModel dataSearchModel = new DataSearchModel();

    public static Context getInstance() {
        return context;
    }
    public DataSearchModel getDataSearchModel() {
        return dataSearchModel;
    }
}
