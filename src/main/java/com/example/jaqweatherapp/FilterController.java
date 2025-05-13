package com.example.jaqweatherapp;

import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import org.controlsfx.control.ToggleSwitch;

import javax.swing.event.ChangeListener;
import java.net.URL;
import java.util.ResourceBundle;

public class FilterController implements Initializable {
    @FXML private GridPane filterList;
    private DataSearchModel model;
    public void initialize(URL location, ResourceBundle resources) {
        model = Context.getInstance().getDataSearchModel();
        initFilters();
    }
    private void initFilters() {
        int filterCount = model.filters.length;
        int rowCount = (int)((filterCount + 1) / 2);
        for(int i = 0; i < filterCount; i++) {
            Text filterName = new Text(model.filters[i].displayName);
            filterName.getStyleClass().add("filterLabel");
            GridPane.setMargin(filterName, new Insets(0, 0, 0, 20));

            ToggleSwitch toggleSwitch = new ToggleSwitch();
            toggleSwitch.setUserData(model.filters[i]);
            toggleSwitch.setSelected(model.filters[i].isSet);
            FilterOption opt = model.filters[i];
            toggleSwitch.selectedProperty().addListener((observable, oldValue, newValue) -> {
                opt.isSet = newValue;
            });
            toggleSwitch.getStyleClass().add("filterToggle");

            filterList.add(filterName, i%2 * 2, i % rowCount);
            filterList.add(toggleSwitch, i%2 * 2 + 1, i % rowCount);
        }
    }
}
