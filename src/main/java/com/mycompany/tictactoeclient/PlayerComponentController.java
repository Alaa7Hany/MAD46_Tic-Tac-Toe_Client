/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.tictactoeclient;

import com.mycompany.tictactoeshared.PlayerDTO;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author LAPTOP
 */
public class PlayerComponentController implements Initializable {
    
    private PlayerDTO player;

    @FXML
    private Label playerName;
    @FXML
    private ImageView onlineLabel;
    @FXML
    private Button challangeBtn;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    @FXML
    private void viewProfile(ActionEvent event) {
        LobbyPageController lobbyController =
            (LobbyPageController)
            challangeBtn.getScene()
                        .getRoot()
                        .getProperties()
                        .get("controller");
        lobbyController.openInvitationDialog(player);
    }
    
    public void setData(PlayerDTO player){
        this.player = player;
        playerName.setText(player.getUsername());
    }

}