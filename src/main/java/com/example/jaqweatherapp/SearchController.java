package com.example.jaqweatherapp;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;

import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

public class SearchController implements Initializable {
    @FXML private ToggleGroup searchModeGroup;
    @FXML private ToggleButton citySearchButton;
    @FXML private ToggleButton coordianteSearchButton;
    @FXML private HBox citySearchView;
    @FXML private HBox coordinateSearchView;

    @FXML private TextField searchBar;
    @FXML private TextField searchLongitude;
    @FXML private TextField searchLatitude;

    private enum SearchType { City, Coordinates };

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        citySearchButton.setUserData(SearchType.City);
        coordianteSearchButton.setUserData(SearchType.Coordinates);
        initNumberInputs();
        Collection<String> coll = Arrays.asList("miasto", "miasto");
        TextFields.bindAutoCompletion(searchBar, coll);
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
    private void initNumberInputs() {
        Pattern pattern = Pattern.compile("-?\\d*(\\.\\d*)?");
        UnaryOperator<TextFormatter.Change> filter = (TextFormatter.Change change) -> {
          String text = change.getControlNewText();
          return pattern.matcher(text).matches() ? change : null;
        };
        TextFormatter<Number> longitudeFormatter = new TextFormatter<>(filter);
        TextFormatter<Number> latitudeFormatter = new TextFormatter<>(filter);
        searchLatitude.setTextFormatter(latitudeFormatter);
        searchLongitude.setTextFormatter(longitudeFormatter);
    }
}
