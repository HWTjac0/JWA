package com.example.jaqweatherapp;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;

import java.net.URL;
import java.util.ResourceBundle;

public class SearchController implements Initializable {
    @FXML
    private ToggleGroup searchModeGroup;
    @FXML
    private ToggleButton citySearchButton;
    @FXML
    private ToggleButton coordianteSearchButton;

    private enum SearchType { City, Coordinates };

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        searchModeGroup.selectedToggleProperty()
                .addListener(new ChangeListener<Toggle>() {
                    @Override
                    public void changed(ObservableValue<? extends Toggle> observableValue, Toggle oldT, Toggle newT) {
                        ToggleButton toggleButton = (ToggleButton) newT;
                        System.out.println(toggleButton.getText());
                    }
                });
        searchModeGroup.selectToggle(citySearchButton);
    }

    private void setSearchMode(SearchType searchMode) {
        switch (searchMode) {
            case City:

                break;
            case Coordinates:
                System.out.println("Coordinates");
                break;
        }
    }
}
