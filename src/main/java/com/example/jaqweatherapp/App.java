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

        Font.loadFont(App.class.getResource("/fonts/Inter.ttf").toExternalForm(), 24);
        Font font = Font.loadFont(App.class.getResource("/fonts/Inter_Black.ttf").toExternalForm(), 24);
        scene.getStylesheets().add(App.class.getResource("/styles/main.css").toExternalForm());

        stage.setUserData("dupa");
        stage.setTitle("JacsWeatherApp");
        stage.setScene(scene);
        stage.setResizable(true);
        stage.show();
        System.out.println(javafx.stage.Window.getWindows().get(0).getUserData());
    }

    public static void main(String[] args) {
        launch();
    }
}