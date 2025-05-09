package com.example.jaqweatherapp;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.ResourceBundle;

public class SearchController implements Initializable {
    @FXML
    private ToggleGroup searchModeGroup;
    @FXML
    private ToggleButton citySearchButton;
    @FXML
    private ToggleButton coordianteSearchButton;
    @FXML
    private HBox citySearchView;
    @FXML
    private HBox coordinateSearchView;


    private enum SearchType { City, Coordinates };

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        citySearchButton.setUserData(SearchType.City);
        coordianteSearchButton.setUserData(SearchType.Coordinates);

        searchModeGroup.selectedToggleProperty()
                .addListener(new ChangeListener<Toggle>() {
                    @Override
                    public void changed(ObservableValue<? extends Toggle> observableValue, Toggle oldT, Toggle newT) {
                        ToggleButton toggleButton = (ToggleButton) newT;
                        if(newT == null) {
                            return;
                        }
                        SearchType searchType = (SearchType) toggleButton.getUserData();
                        toggleView(searchType);
                    }
                });
        searchModeGroup.selectToggle(citySearchButton);
    }
    private void toggleView(SearchType currentSearchType) {
        switch (currentSearchType) {
            case City:
                coordinateSearchView.setVisible(false);
                coordinateSearchView.setManaged(false);
                citySearchView.setVisible(true);
                citySearchView.setManaged(true);
                break;
            case Coordinates:
                coordinateSearchView.setVisible(true);
                coordinateSearchView.setManaged(true);
                citySearchView.setVisible(false);
                citySearchView.setManaged(false);
                break;
        }
    }
}
