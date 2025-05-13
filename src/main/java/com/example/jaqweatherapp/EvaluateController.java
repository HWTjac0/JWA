package com.example.jaqweatherapp;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import java.net.URL;
import java.util.ResourceBundle;

public class EvaluateController implements Initializable {
    @FXML Button fetchButton;
    DataSearchModel dataSearchModel = Context.getInstance().getDataSearchModel();
    public void initialize(URL location, ResourceBundle resources) {
        fetchButton.setOnAction(event -> {
            for(FilterOption opt : dataSearchModel.filters) {
                if(!opt.isSet) {
                    continue;
                }
                System.out.println(opt.displayName + " " + opt.isSet);
            }
        });
    }
}
