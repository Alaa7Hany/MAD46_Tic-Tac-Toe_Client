/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tictactoeclient;


import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
/**
 * FXML Controller class
 *
 * @author emada
 */
public class LoginPageController implements Initializable {


    @FXML
    private TextField userNameTextField;
    @FXML
    private PasswordField passwordTextField;
    @FXML
    private Button loginButton;
    @FXML
    private Button signUpNav;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
       userNameTextField.setOnKeyTyped(event -> Sounds.playTypeClick());
       passwordTextField.setOnKeyTyped(event -> Sounds.playTypeClick());
       
    }    
    
    @FXML
    private void login(ActionEvent event) {
    }

    @FXML
    private void navigateToSignUp(ActionEvent event) {
    }

}
