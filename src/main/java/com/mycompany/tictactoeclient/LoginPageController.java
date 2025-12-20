/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tictactoeclient;


import com.mycompany.tictactoeclient.network.NetworkDAO;
import com.mycompany.tictactoeshared.PlayerDTO;
import com.mycompany.tictactoeshared.Response;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
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
        String username = userNameTextField.getText();
        String password = passwordTextField.getText();
        
        
        // new thread because we can't for response on the main thread
        new Thread(() -> {
            
            // while waiting we should show like a circular progress indicator
            
           Response response = NetworkDAO.getInstance().login(username, password);
           
           Platform.runLater(() -> {
               if(response.getStatus() == Response.Status.SUCCESS){
                    System.out.println("Login Successful");
                    PlayerDTO player = (PlayerDTO) response.getData();
                    String name = player.getUsername();
                    System.out.println("Helloooooooooo"+name);
                    // Navigation to Lobby
                    
                    try {
                        FXMLLoader loader = new FXMLLoader(
                            getClass().getResource("/com/mycompany/tictactoeclient/LobbyPage.fxml")
                        );

                        Parent root = loader.load();

                        LobbyPageController controller = loader.getController();
                        controller.setCurrentPlayer(player);
                        Stage stage = (Stage) loginButton.getScene().getWindow();
                        stage.setScene(new Scene(root));
                        stage.show();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
               }else{
                   System.out.println("Login Failed");
                   // Maybe show error Message
               }
           });
        }).start();
    }

    @FXML
    private void navigateToSignUp(ActionEvent event) {
    }

}
