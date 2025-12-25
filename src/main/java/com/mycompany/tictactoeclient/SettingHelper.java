/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tictactoeclient;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/**
 *
 * @author LAPTOP
 */
public class SettingHelper {

    private VBox settingsView;
    private boolean visible = false;
    private StackPane layer;
    private boolean isStartPage;

    public SettingHelper(StackPane layer, boolean isStartPage) {
        this.layer = layer;
        this.isStartPage = isStartPage;
    }

    public void toggle() {
        if (visible) {
            layer.getChildren().clear();
            layer.setMouseTransparent(true);
            visible = false;
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("settings.fxml")
            );
            settingsView = loader.load();

            SettingsController controller = loader.getController();
            if (isStartPage) {
                controller.hideLogout();
            }

            layer.getChildren().add(settingsView);
            StackPane.setAlignment(settingsView, Pos.TOP_LEFT);
            StackPane.setMargin(settingsView, new Insets(80, 0, 0, 20));

            layer.setMouseTransparent(false);
            settingsView.setMouseTransparent(false);

            layer.setOnMouseClicked(e -> toggle());
            settingsView.setOnMouseClicked(e -> e.consume());

            layer.toFront();
            visible = true;

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
