/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.tictactoeclient;

iimport javafx.event.ActionEvent;
import javafx.fxml.FXML;
mport javafx.fxml.Initializable;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

import java.net.URL;

import java.net.URL;

/**
 * FXML Controller class
 *
 * @author LAPTOP
 */
public class PlayerComponentController implements Initializable {


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
    }

}