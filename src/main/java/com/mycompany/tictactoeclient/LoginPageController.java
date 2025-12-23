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
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;

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
    @FXML
    private StackPane rootStackPane;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
       userNameTextField.setOnKeyTyped(event -> Sounds.playTypeClick());
       passwordTextField.setOnKeyTyped(event -> Sounds.playTypeClick());
       
    }    
    
    // Emad ... need this to pass player dto 
    @FXML
    private void login(ActionEvent event) {
        String username = userNameTextField.getText();
        String password = passwordTextField.getText();
        
        if(username.isEmpty() || password.isEmpty()){
            Platform.runLater(()->{
                App.showAlertMessage(rootStackPane, "Please fill your data", false);
            });
            return;
        }
            
        // new thread because we can't wait for response on the main thread
        new Thread(() -> {
            
            // while waiting we should show like a circular progress indicator
            App.addProgressIndicator(rootStackPane);
            
            
            Response response = NetworkDAO.getInstance().login(username, password); 
           
            App.removeProgressIndicator(rootStackPane);
            
            
            if(response.getStatus() == Response.Status.SUCCESS){
                 System.out.println("#################### Login Successful");
                 PlayerDTO player = (PlayerDTO) response.getData();
                 String name = player.getUsername();
                 System.out.println("######################### Helloooooooooo "+name);
                 // Navigation to Lobby
                 Platform.runLater(()->{
                     App.showAlertMessage(rootStackPane, "Login Successful!", true);
                 });
                 
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ex) {
                    System.getLogger(LoginPageController.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
                }
                 
                Platform.runLater(()->{
                    try {
                        // ############# Navigation to lobby
//                  App.setRoot(Pages.lobbyPage);
                    FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/mycompany/tictactoeclient/LobbyPage.fxml"));

                    Parent root = loader.load();
                    LobbyPageController controller = loader.getController();

                    Stage stage = (Stage) loginButton.getScene().getWindow();
                    stage.getScene().setRoot(root);
                    controller.setCurrentPlayer(player);
                    } catch (IOException ex) {
                        System.getLogger(LoginPageController.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
                    }
                });
            }else{
                 System.out.println("Login Failed");
                 Platform.runLater(()->{
                     App.showAlertMessage(rootStackPane, "Login Failed!", false);
                 });
            }
               
        }).start();
    }

    @FXML
    private void navigateToSignUp(ActionEvent event) {
        try {
            App.setRoot(Pages.signUpPage);
        } catch (IOException ex) {
            System.getLogger(LocalOrOnlineDialogController.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }

}
