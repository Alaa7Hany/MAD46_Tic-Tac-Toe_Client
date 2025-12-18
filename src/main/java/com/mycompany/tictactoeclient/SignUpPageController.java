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
 *
 * @author emada
 */
public class SignUpPageController implements Initializable {
    
    @FXML
    private PasswordField confirmPasswordTextField;

    @FXML
    private Button loginNav;

    @FXML
    private PasswordField passwordTextField;

    @FXML
    private Button signUpButton;

    @FXML
    private TextField userNameTextField;
    
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
       userNameTextField.setOnKeyTyped(event -> Sounds.playTypeClick());
       passwordTextField.setOnKeyTyped(event -> Sounds.playTypeClick());
       confirmPasswordTextField.setOnKeyTyped(event -> Sounds.playTypeClick());
       
    } 
    
    
    @FXML
    void navigateToLogin(ActionEvent event) {

    }

    @FXML
    void signUp(ActionEvent event) {

    }
    
    
}
