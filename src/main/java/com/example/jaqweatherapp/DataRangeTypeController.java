package com.example.jaqweatherapp;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.util.Callback;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class DataRangeTypeController implements Initializable {
    @FXML private ToggleGroup dataRangeTypeGroup;
    @FXML private ToggleButton currentDataButton;
    @FXML private ToggleButton historicDataButton;
    @FXML private HBox currentDataView;
    @FXML private HBox historicDataView;
    @FXML private ComboBox<ComboOption> currentDataForecast;
    @FXML private ComboBox<ComboOption> currentDataPastDays;
    @FXML private DatePicker historicDataBegin;
    @FXML private DatePicker historicDataEnd;

    private enum DataRangeType { Current, Historic };
    public void initialize(URL location, ResourceBundle resources) {
        initCurrentDataForecast();
        initCurrentDataPastDays();
        initHistoricData();

        currentDataButton.setUserData(DataRangeType.Current);
        historicDataButton.setUserData(DataRangeType.Historic);
        dataRangeTypeGroup.selectedToggleProperty()
                .addListener(new ChangeListener<Toggle>() {
                    @Override
                    public void changed(ObservableValue<? extends Toggle> observableValue, Toggle oldT, Toggle newT) {
                        ToggleButton toggleButton = (ToggleButton) newT;
                        if(newT == null) {
                            return;
                        }
                        DataRangeType dataType = (DataRangeType) toggleButton.getUserData();
                        toggleView(dataType);
                    }
                });
        dataRangeTypeGroup.selectToggle(currentDataButton);
    }
    private void toggleView(DataRangeType dataType) {
        switch(dataType) {
            case Current:
                historicDataView.setVisible(false);
                historicDataView.setManaged(false);
                currentDataView.setVisible(true);
                currentDataView.setManaged(true);
                break;
            case Historic:
                currentDataView.setVisible(false);
                currentDataView.setManaged(false);
                historicDataView.setVisible(true);
                historicDataView.setManaged(true);
                break;
        }
    }
    private void initCurrentDataForecast() {
        ObservableList<ComboOption> comboOptions = FXCollections.observableArrayList(
                new ComboOption("1 Dzień", "1"),
                new ComboOption("3 Dni", "3"),
                new ComboOption("7 Dni", "7"),
                new ComboOption("14 Dni", "14"),
                new ComboOption("16 Dni", "16")
        );
        currentDataForecast.setItems(comboOptions);
        currentDataForecast.getSelectionModel().select(0);
    }
    private void initCurrentDataPastDays() {
        ObservableList<ComboOption> comboOptions = FXCollections.observableArrayList(
                new ComboOption("0 Dni", "0"),
                new ComboOption("1 Dzień", "1"),
                new ComboOption("2 Dzień", "2"),
                new ComboOption("3 Dzień", "3"),
                new ComboOption("5 Dzień", "5"),
                new ComboOption("1 Tydzień", "7"),
                new ComboOption("2 Tygodnie", "14"),
                new ComboOption("1 Miesiąc", "30"),
                new ComboOption("2 Miesiące", "60"),
                new ComboOption("3 Miesiące", "90")

        );
        currentDataPastDays.setItems(comboOptions);
        currentDataPastDays.getSelectionModel().select(0);
    }
    private void initHistoricData() {
        historicDataBegin.setValue(LocalDate.now().minusDays(1));
        historicDataEnd.setValue(LocalDate.now());
        historicDataBegin.setShowWeekNumbers(true);
        historicDataEnd.setShowWeekNumbers(true);
        Callback<DatePicker, DateCell> beginRangeFactory = new DateRangeFactory(89, -1);
        Callback<DatePicker, DateCell> endRangeFactory = new DateRangeFactory(0, 15);
        historicDataBegin.setDayCellFactory(beginRangeFactory);
        historicDataEnd.setDayCellFactory(endRangeFactory);
    }
}

class DateRangeFactory implements Callback<DatePicker, DateCell> {
    private final int pastDaysAllowed;
    private final int futureDaysAllowed;
    public DateRangeFactory(int pastDaysAllowed, int futureDaysAllowed) {
        this.pastDaysAllowed = pastDaysAllowed;
        this.futureDaysAllowed = futureDaysAllowed;
    }
    @Override
    public DateCell call(DatePicker datePicker) {
        return new DateCell() {
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                LocalDate today = LocalDate.now();
                LocalDate pastDaysLimit = today.minusDays(pastDaysAllowed);
                LocalDate futureDaysLimit = today.plusDays(futureDaysAllowed);
                if(item.isBefore(pastDaysLimit) || item.isAfter(futureDaysLimit)) {
                    setDisable(true);
                    setStyle("-fx-background-color: #9f9f9f");
                }
            }
        };
    }

}