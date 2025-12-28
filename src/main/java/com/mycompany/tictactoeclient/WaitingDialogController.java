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

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
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
    
    @FXML
    private void exitAction(ActionEvent event) {
        if (LobbyPageController.instance != null) {
            LobbyPageController.instance.closeWaitingDialog();
        }
        
    }

}
