/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.tictactoeclient;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;

/**
 * FXML Controller class
 *
 * @author siam
 */
public class WaitingDialogController implements Initializable {

    @FXML
    private Button exitBtn;
    @FXML
    private Label name;
    @FXML
    private AnchorPane root;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    public void setPlayerName(String playerName) {
        name.setText(playerName);
    }

    @FXML
    private void exitAction(ActionEvent event) {
        // If lobby controller is available
        if (LobbyPageController.instance != null) {
            LobbyPageController.instance.closeWaitingDialog();
        }

        //remove from game page
        if (root != null && root.getParent() instanceof StackPane) {
            StackPane parent = (StackPane) root.getParent();
            Node dimmer = null;
            for (Node n : parent.getChildren()) {
                if (n instanceof Region && "waitingDialogDimmer".equals(n.getId())) {
                    dimmer = n;
                    break;
                }
            }
            if (dimmer != null) {
                parent.getChildren().remove(dimmer);
            }

            parent.getChildren().remove(root);
        }

    }

}
