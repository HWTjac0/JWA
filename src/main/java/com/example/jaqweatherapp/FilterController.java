package com.example.jaqweatherapp;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import org.controlsfx.control.ToggleSwitch;

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
        for(int i = 0; i < model.filters.length; i++) {
            Text filterName = new Text(model.filters[i].displayName);
            GridPane.setMargin(filterName, new Insets(0, 0, 0, 20));
            ToggleSwitch toggleSwitch = new ToggleSwitch();
            filterList.add(filterName, i%2 * 2, i % 5);
            filterList.add(toggleSwitch, i%2 * 2 + 1, i % 5);
        }
    }
}
