/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tictactoeclient;

import javafx.fxml.FXMLLoader;
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
        } else {
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
                layer.setMouseTransparent(false);
                //layer.setStyle("-fx-background-color: rgba(0,0,0,0.3);");
                layer.toFront();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        visible = !visible;
    }
}
