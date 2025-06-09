package com.example.jaqweatherapp;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.Optional;

public class RootController {
    // It shouldnt be here
    @FXML
    public void openDialog(){
        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource("settings-dialog-view.fxml"));
            Parent dialogContent = loader.load();
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setTitle("Ustawienia");
            dialog.setHeaderText("Dostosuj parametry");

            DialogPane dialogPane = dialog.getDialogPane();
            dialogPane.setContent(dialogContent);
            //dialogPane.getStylesheets().add(RootController.class.getResource("main.css").toExternalForm());
            ButtonType applyButton = new ButtonType("Zastosuj", ButtonBar.ButtonData.OK_DONE);
            ButtonType cancelButton = new ButtonType("Anuluj", ButtonBar.ButtonData.CANCEL_CLOSE);

            dialogPane.getButtonTypes().addAll(applyButton, cancelButton);

            Optional<ButtonType> result = dialog.showAndWait();

            if (result.isPresent() && result.get() == applyButton) {
                System.out.println("OK");
            } else {
                System.out.println("Settings dialog cancelled.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
