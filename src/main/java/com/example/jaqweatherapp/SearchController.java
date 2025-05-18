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
import java.util.*;
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
    SearchModel searchModel = Context.getInstance().getSearchModel();

    public enum SearchType { City, Coordinates };

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
                        searchModel.currentSearchType = searchType;
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

        searchLatitude.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.trim().isEmpty()) {
                searchModel.areCoordsEmpty = true;
                return;
            }
            searchModel.coordinates.latitude = Double.parseDouble(newValue);
            if(!searchLongitude.getText().trim().isEmpty()) {
                searchModel.areCoordsEmpty = false;
            }
        });
        searchLongitude.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.trim().isEmpty()) {
                searchModel.areCoordsEmpty = true;
                return;
            }
            searchModel.coordinates.longitude = Double.parseDouble(newValue);
            if(!searchLatitude.getText().trim().isEmpty()) {
                searchModel.areCoordsEmpty = false;
            }
        });
    }
    private void initLocationGeocoder() {
        GeocodingSuggestionProvider suggestionProvider = new GeocodingSuggestionProvider(geocodingApiClient);
        AutoCompletionBinding<String> autoCompleter = TextFields.bindAutoCompletion(searchBar, suggestionProvider);
        autoCompleter.setDelay(200);
        autoCompleter.setOnAutoCompleted(event -> {
            String selected = event.getCompletion();
            Location location = suggestionProvider.getByDisplayName(selected);
            searchLatitude.setText(String.format("%.2f", location.latitude));
            searchLongitude.setText(String.format("%.2f", location.longitude));
            searchModel.coordinates = new Coordinates(location.latitude, location.longitude);
            searchModel.locationName = selected;
        });
    }
}

class GeocodingSuggestionProvider implements Callback<ISuggestionRequest, Collection<String>> {
    GeocodingApiClient geocodingApiClient;
    private Map<String, Location> cache = new WeakHashMap<>();
    public GeocodingSuggestionProvider(GeocodingApiClient geocodingApiClient) {
        this.geocodingApiClient = geocodingApiClient;
    }
    @Override
    public Collection<String> call(ISuggestionRequest request) {
        String text = request.getUserText();
        cache.clear();
        if (text == null || text.trim().isEmpty()) {
            return null;
        }
        CompletableFuture<List<Location>> futureLocations = geocodingApiClient.getAddresses(text);
        return futureLocations.thenApply(addresses -> {
            return addresses.stream()
                    .map(l -> {
                        cache.put(l.displayName, l);
                        return l.displayName;
                    })
                    .toList();
                }
        ).exceptionally(error -> {
            System.out.println("Error: " + error);
            return null;
        }).join();
    }
    public Location getByDisplayName(String displayName) {
        return cache.get(displayName);
    }
}