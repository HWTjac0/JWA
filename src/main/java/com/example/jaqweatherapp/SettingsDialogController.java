package com.example.jaqweatherapp;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.DirectoryChooser;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class SettingsDialogController implements Initializable {
    @FXML private ToggleGroup settingTemp;
    @FXML private ToggleGroup settingMeasur;
    @FXML private ToggleGroup settingSpeed;

    @FXML private HBox settingTempButtons;
    @FXML private HBox settingMeasurButtons;
    @FXML private HBox settingSpeedButtons;

    @FXML private Button dirButton;
    @FXML private Label dirLabel;
    SettingsModel settingsModel = Context.getInstance().getSettingsModel();
    UnitManager unitManager = settingsModel.unitManager;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initDirChooser();
        initButtons();
    }
    private void initDirChooser(){
        dirLabel.setText(settingsModel.defaultExportPath.toString());
        DirectoryChooser directoryChooser = new DirectoryChooser();
        dirButton.setOnAction(event -> {
            File selectedDirectory = directoryChooser.showDialog(null);
        });

    }
    private void initButtons() {
        // I could automate this maybe later somehow:P
        ToggleButton celsiusButton = new ToggleButton(Unit.Celsius.toString());
        celsiusButton.setUserData(Unit.Celsius);
        ToggleButton fahrenheitButton = new ToggleButton(Unit.Fahrenheit.toString());
        fahrenheitButton.setUserData(Unit.Fahrenheit);
        settingTempButtons.getChildren().clear();
        settingTempButtons.getChildren().addAll(celsiusButton, fahrenheitButton);
        settingTemp.getToggles().addAll(celsiusButton, fahrenheitButton);
        settingTemp.selectToggle(switch (unitManager.currentUnits.get(UnitType.TEMPERATURE)) {
            case Fahrenheit -> fahrenheitButton;
            default -> celsiusButton;
        });
        for(Node button : settingTempButtons.getChildren()) {
            button.setOnMouseClicked(event -> {
                unitManager.currentUnits.put(UnitType.TEMPERATURE, (Unit) button.getUserData());
            });
        }

        ToggleButton milimeterButton = new ToggleButton(Unit.Millimeters.toString());
        milimeterButton.setUserData(Unit.Millimeters);
        ToggleButton inchesButton = new ToggleButton(Unit.Inches.toString());
        inchesButton.setUserData(Unit.Inches);
        settingMeasurButtons.getChildren().clear();
        settingMeasurButtons.getChildren().addAll(milimeterButton, inchesButton);
        settingMeasur.getToggles().addAll(milimeterButton, inchesButton);
        settingMeasur.selectToggle(switch (unitManager.currentUnits.get(UnitType.PRECIPITATION)) {
            case Inches -> inchesButton;
            default -> milimeterButton;
        });
        for(Node button : settingMeasurButtons.getChildren()) {
            button.setOnMouseClicked(event -> {
                unitManager.currentUnits.put(UnitType.PRECIPITATION, (Unit) button.getUserData());
            });
        }

        ToggleButton kmperhButton = new ToggleButton(Unit.KmPerHour.toString());
        kmperhButton.setUserData(Unit.KmPerHour);
        ToggleButton mpersButton = new ToggleButton(Unit.MPerSecond.toString());
        mpersButton.setUserData(Unit.MPerSecond);
        ToggleButton knotsButton = new ToggleButton(Unit.Knots.toString());
        knotsButton.setUserData(Unit.Knots);
        ToggleButton mphButton =  new ToggleButton(Unit.MilesPerHour.toString());
        mphButton.setUserData(Unit.MilesPerHour);
        settingSpeedButtons.getChildren().clear();
        settingSpeed.getToggles().addAll(kmperhButton, mpersButton, knotsButton, mphButton);
        settingSpeedButtons.getChildren().addAll(kmperhButton, mpersButton, knotsButton, mphButton);
        settingSpeed.selectToggle(switch (unitManager.currentUnits.get(UnitType.SPEED)) {
            case Knots -> knotsButton;
            case MilesPerHour -> mphButton;
            case MPerSecond -> mpersButton;
            default -> kmperhButton;
        });
        for(Node button : settingSpeedButtons.getChildren()) {
            button.setOnMouseClicked(event -> {
                unitManager.currentUnits.put(UnitType.SPEED, (Unit) button.getUserData());
            });
        }
    }
}
