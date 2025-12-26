/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tictactoeclient;

import com.mycompany.tictactoeclient.enums.SettingsPosition;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class SettingHelper {

    private VBox settingsView;
    private boolean visible = false;
    private StackPane layer;
    private SettingsPosition position;

    public SettingHelper(StackPane layer, SettingsPosition position) {
        this.layer = layer;
        this.position = position;
    }

    public void toggle() {
        if (visible) {
            layer.getChildren().clear();
            layer.setVisible(false);
            layer.setManaged(false);
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

            if (position == SettingsPosition.TOP) {
                controller.hideLogout();
                layer.setOnMouseClicked(e -> toggle());
            }

            layer.getChildren().add(settingsView);
            layer.setVisible(true);
            layer.setManaged(true);
            layer.setMouseTransparent(false);

            if (position == SettingsPosition.TOP) {
                StackPane.setAlignment(settingsView, Pos.TOP_LEFT);
                StackPane.setMargin(settingsView, new Insets(80, 0, 0, 20));
            } else {
                StackPane.setAlignment(settingsView, Pos.BOTTOM_RIGHT);
                StackPane.setMargin(settingsView, new Insets(0, 20, 80, 0));
            }

            settingsView.setMouseTransparent(false);
            settingsView.setOnMouseClicked(e -> e.consume());

            layer.toFront();
            visible = true;

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
