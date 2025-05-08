package com.example.jaqweatherapp;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import java.net.URL;
import java.util.ResourceBundle;

public class SearchController implements Initializable {
    @FXML
    private Button citySearchButton;
    @FXML
    private Button coordianteSearchButton;

    private enum SearchType { City, Coordinates };

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        citySearchButton.setOnAction(event -> {setSearchMode(SearchType.City);});
        coordianteSearchButton.setOnAction(event -> {setSearchMode(SearchType.Coordinates);});
        setSearchMode(SearchType.City);
    }

    private void setSearchMode(SearchType searchMode) {
        switch (searchMode) {
            case City:
                System.out.println("City");
                break;
            case Coordinates:
                System.out.println("Coordinates");
                break;
        }
    }
}
