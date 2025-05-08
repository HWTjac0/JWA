package com.example.jaqweatherapp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("main.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 640, 480);

        Font.loadFont(App.class.getResource("/fonts/Inter_24pt-Regular.ttf").toExternalForm(), 24);
        scene.getStylesheets().add(App.class.getResource("/styles/main.css").toExternalForm());

        stage.setTitle("JacsWeatherApp");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}