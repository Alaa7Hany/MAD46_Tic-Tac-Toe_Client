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
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

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
    @FXML
    private StackPane rootStackPane;
    
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
       userNameTextField.setOnKeyTyped(event -> Sounds.playTypeClick());
       passwordTextField.setOnKeyTyped(event -> Sounds.playTypeClick());
       confirmPasswordTextField.setOnKeyTyped(event -> Sounds.playTypeClick());
       
    } 
    
    
    @FXML
    void navigateToLogin(ActionEvent event) {
        try {
            App.setRoot(Pages.loginPage);
        } catch (IOException ex) {
            System.getLogger(LocalOrOnlineDialogController.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }

    @FXML
    void signUp(ActionEvent event) {
        String username = userNameTextField.getText();
        String password = passwordTextField.getText();
        String confirmPassword = confirmPasswordTextField.getText();
        
        if(username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()){
            Platform.runLater(()->{
                App.showAlertMessage(rootStackPane, "Please fill your data", false);
            });
            return;
        }
        
        if(!password.equals(confirmPassword)){
            Platform.runLater(()->{
                App.showAlertMessage(rootStackPane, "Passwords Don't match", false);
            });
            return;
        }
        if(password.length()<6){
            Platform.runLater(()->{
                App.showAlertMessage(rootStackPane, "Password length must be at least 6", false);
            });
            return;
        }
            
        // new thread because we can't wait for response on the main thread
        new Thread(() -> {
            
            App.addProgressIndicator(rootStackPane);

            Response responseTemp;
            try {
                responseTemp = NetworkDAO.getInstance().register(username, password); 
            } catch (Exception e) {
                System.out.println(e);
                responseTemp = new Response(Response.Status.FAILURE, "Something went wrong");
            }finally{
                App.removeProgressIndicator(rootStackPane);
            }
            
            final Response response = responseTemp;

            if(response.getStatus() == Response.Status.SUCCESS){
                 System.out.println("#################### Register Successful");
                 PlayerDTO player = (PlayerDTO) response.getData();
                 String name = player.getUsername();
                 System.out.println("######################### Helloooooooooo "+name);
                 // Navigation to Lobby
                 Platform.runLater(()->{
                     App.showAlertMessage(rootStackPane, "Register Successful!", true);
                 });
                 
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ex) {
                    System.getLogger(LoginPageController.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
                }
                 
                Platform.runLater(()->{
                    try {
                    FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/mycompany/tictactoeclient/LobbyPage.fxml"));

                    Parent root = loader.load();
                    LobbyPageController controller = loader.getController();

                    Stage stage = (Stage) signUpButton.getScene().getWindow();
                    stage.getScene().setRoot(root);
                    controller.setCurrentPlayer(player);
                    } catch (IOException ex) {
                        System.getLogger(LoginPageController.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
                    }
                });
            }else{
                 System.out.println("Register Failed");
                 Platform.runLater(()->{
                     App.showAlertMessage(rootStackPane, (String) response.getData(), false);
                 });
            }           
        }).start();
    }
    
    
}
