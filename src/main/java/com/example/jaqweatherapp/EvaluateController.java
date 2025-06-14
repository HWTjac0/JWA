package com.example.jaqweatherapp;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.prefs.Preferences;

public class EvaluateController implements Initializable {
    @FXML Button fetchButton;
    @FXML Button importButton;
    @FXML Label loadingStatus;
    FilterModel filterModel = Context.getInstance().getFilterModel();
    SearchModel searchModel = Context.getInstance().getSearchModel();
    DateRangeModel dateRangeModel = Context.getInstance().getDateRangeModel();
    WeatherApiClient weatherApiClient = Context.getInstance().getWeatherApiClient();
    GeocodingApiClient geocodingApiClient = Context.getInstance().getGeocodingApiClient();
    ForecastModel forecastModel = Context.getInstance().getForecastModel();
    ApiParameters apiParameters = new ApiParameters();
    CacheManager cacheManager = Context.getInstance().getCacheManager();
    Preferences prefs = Preferences.userRoot().node("/com/hwtjac0/JaqWeatherApp");

    enum LoadingStatus {
        Loading(Color.YELLOW, "Ładowanie..."),
        Success(Color.GREEN, "Załadowano dane pomyślnie!"),
        Failure_Wrong_File(Color.RED, "Zły format pliku!"),
        Failure_Timeout(Color.RED, "Nie można pobrać danych z serwisu! Spróbuj później"),
        Failure_Incomplete_Data(Color.RED, "Uzupełnij dane!"),
        Failure_Incomplete_Filters(Color.RED, "Musisz wybrać jakie dane chcesz sprawdzić!"),
        Failure_Incomplete_Address(Color.RED, "Uzupełnij adres!"),
        Failure_Incomplete_Coords(Color.RED, "Uzupełnij współrzędne!"),
        Failure_Wrong_Coords(Color.RED, "Wprowadź poprawne współrzędne!"),
        Failure_Wrong_Address(Color.RED, "Wprowadź poprawne adres!");
        public final Color color;
        public final String message;
        LoadingStatus(Color color, String message) {
            this.color = color;
            this.message = message;
        }
    }
    private void setLoadingStatus(LoadingStatus status) {
        loadingStatus.setText(status.message);
        loadingStatus.setTextFill(status.color);
    }
    public void initialize(URL location, ResourceBundle resources) {
        fetchButton.setOnAction(event -> {
            setLoadingStatus(LoadingStatus.Loading);
            fetchFromApi();
        });
        importButton.setOnAction(event -> {
            setLoadingStatus(LoadingStatus.Loading);
            fetchFromFile();
        });

    }
    private void fetchFromFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Json Files", "*.json"),
                new FileChooser.ExtensionFilter("Text Files", "*.txt")
        );
        fileChooser.setInitialDirectory(new File(prefs.get("export_path", SettingsModel.getDefaultExportPath().toString())));
        File importFile = fileChooser.showOpenDialog(null);
        if(importFile == null) {
            return;
        }
        ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.readValue(importFile, ForecastModel.class);
            createChartPopup();
        } catch (IOException e) {
            setLoadingStatus(LoadingStatus.Failure_Wrong_File);
        }

    }

    private void setDataRangeParams(ApiParameters params) {
        switch (dateRangeModel.dataRangeType) {
            case Forecast -> {
                params.add("past_days", dateRangeModel.forecastPastDays);
                params.add("forecast_days", dateRangeModel.forecastFutureDays);
            }
            case Historic -> {
                params.add("start_date", dateRangeModel.historicStartDate.toString());
                params.add("end_date", dateRangeModel.historicEndDate.toString());
            }
        }
    }

    private Boolean setFilterParams(ApiParameters params) {
        params.add("hourly", "");
        for (FilterOption opt : filterModel.filters) {
            if (!opt.isSet) {
                continue;
            }
            String val = params.get("hourly") + (params.get("hourly").isEmpty() ? "" : ",") + opt.value;
            params.add("hourly", val);
        }
        if(params.get("hourly").isEmpty()) {
            setLoadingStatus(LoadingStatus.Failure_Incomplete_Filters);
            return false;
        }
        String currentTemp = switch (Unit.get(prefs.get("unit_temperature", Unit.Celsius.toString()))) {
            case Unit.Fahrenheit -> "fahrenheit";
            default -> "celsius";
        };
        String currentPrec = switch (Unit.get(prefs.get("unit_precipation", Unit.Millimeters.toString()))) {
            case Unit.Inches -> "inch";
            default -> "mm";
        };
        String currentSpeed = switch (Unit.get(prefs.get("unit_speed", Unit.KmPerHour.toString()))) {
            case Unit.MPerSecond -> "ms";
            case Unit.Knots -> "kn";
            case Unit.MilesPerHour -> "mph";
            default -> "kmh";
        };
        if (searchModel.currentSearchType == SearchController.SearchType.City
                && searchModel.areCoordsEmpty) {
            if (searchModel.isLocationEmpty) {
                setLoadingStatus(LoadingStatus.Failure_Incomplete_Address);
                return false;
            }
            try {
                List<Location> locations = geocodingApiClient.getAddresses(searchModel.locationName).join();
                if(locations.isEmpty()) {
                    setLoadingStatus(LoadingStatus.Failure_Wrong_Address);
                    return false;
                }
                Location l = locations.getFirst();
                searchModel.coordinates = new Coordinates(l.latitude, l.longitude);
                searchModel.areCoordsEmpty = false;
            } catch (Exception e) {
                setLoadingStatus(LoadingStatus.Failure_Timeout);
            }
        } else if (searchModel.currentSearchType == SearchController.SearchType.Coordinates
                && searchModel.areCoordsEmpty) {
            setLoadingStatus(LoadingStatus.Failure_Incomplete_Coords);
            return false;
        } else if(searchModel.currentSearchType == SearchController.SearchType.Coordinates
                && !searchModel.areCoordsCorrect()) {
            setLoadingStatus(LoadingStatus.Failure_Wrong_Coords);
            return false;
        }
        params.add("wind_speed_unit", currentSpeed);
        params.add("temperature_unit", currentTemp);
        params.add("precipitation_unit", currentPrec);
        return true;
    }
    private void createChartPopup() {
        setLoadingStatus(LoadingStatus.Success);
        Parent root;
        try {
            root = new FXMLLoader(App.class.getResource("chart-view.fxml")).load();
            Font.loadFont(EvaluateController.class.getResource("/fonts/Inter.ttf").toExternalForm(), 24);
            root.getStylesheets().add(App.class.getResource("/styles/chartpopup.css").toExternalForm());
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();
    }

    private void fetchFromApi() {

        apiParameters.clear();

        if (!setFilterParams(apiParameters)) return;
        setDataRangeParams(apiParameters);

        apiParameters.add("latitude", String.valueOf(searchModel.coordinates.latitude));
        apiParameters.add("longitude", String.valueOf(searchModel.coordinates.longitude));
        ObjectMapper mapper = new ObjectMapper();
        Task<ForecastModel> forecastTask = new Task<>() {
            @Override
            protected ForecastModel call() throws Exception {
                forecastModel.dateSeries.clear();
                forecastModel.dataMap.clear();
                Result<String> res = cacheManager.getCache(apiParameters.getHash());
                if (!res.valid()) {
                    try {
                        CompletableFuture<String> modelPromise = weatherApiClient.getForecast(apiParameters.getParameters());
                        String response = modelPromise.join();
                        CacheTTL expiration = CacheTTL.Hour;
                        if (dateRangeModel.dataRangeType == DateRangeModel.DataRangeType.Historic &&
                                dateRangeModel.historicEndDate.isBefore(LocalDate.now())) {
                            expiration = CacheTTL.Infinite;
                        }
                        cacheManager.setCache(
                                apiParameters.getHash(),
                                response,
                                expiration,
                                weatherApiClient.baseUrl + weatherApiClient.buildQueryString(apiParameters.getParameters()));
                        try {
                            // It implicitly writes to global ForeastModel
                            return mapper.readValue(response, ForecastModel.class);
                        } catch (Exception e) {
                            setLoadingStatus(LoadingStatus.Failure_Timeout);
                            e.printStackTrace();
                        }
                    } catch (Exception e) {
                        setLoadingStatus(LoadingStatus.Failure_Timeout);
                    }
                } else {
                    return mapper.readValue(res.data(), ForecastModel.class);
                }
                return null;
            }
        };
        forecastTask.setOnSucceeded(e -> {
            createChartPopup();
        });
        new Thread(forecastTask).start();
    }
}
