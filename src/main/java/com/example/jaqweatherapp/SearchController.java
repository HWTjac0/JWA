package com.example.jaqweatherapp;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.AutoCompletionBinding.ISuggestionRequest;
import org.controlsfx.control.textfield.TextFields;

import java.net.URL;
import java.util.Collection;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class SearchController implements Initializable {
    @FXML private ToggleGroup searchModeGroup;
    @FXML private ToggleButton citySearchButton;
    @FXML private ToggleButton coordinateSearchButton;
    @FXML private HBox citySearchView;
    @FXML private HBox coordinateSearchView;

    @FXML private TextField searchBar;
    @FXML private TextField searchLongitude;
    @FXML private TextField searchLatitude;

    GeocodingApiClient geocodingApiClient = Context.getInstance().getGeocodingApiClient();

    private enum SearchType { City, Coordinates };

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        citySearchButton.setUserData(SearchType.City);
        coordinateSearchButton.setUserData(SearchType.Coordinates);
        initCoordInputs();
        initViewToggle();
        initLocationGeocoder();
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
    private void initViewToggle() {
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
    private void initCoordInputs() {
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
    private void initLocationGeocoder() {
        AutoCompletionBinding<String> autoCompleter =
                TextFields.bindAutoCompletion(searchBar,  request -> {
                    String text = request.getUserText();
                    if (text == null || text.trim().isEmpty()) {
                        return null;
                    }
                    CompletableFuture<List<Location>> futureLocations = geocodingApiClient.getAddresses(text);

                    return futureLocations.thenApply(addresses -> {
                        List<String> newAddresses =  addresses.stream()
                                        .map(l -> l.displayName)
                                        .toList();
                        System.out.println(newAddresses);
                        return newAddresses;
                            }
                    ).exceptionally(error -> {
                        System.out.println("Error: " + error);
                        return null;
                    }).join();
                });
        autoCompleter.setDelay(300);
    }
}
