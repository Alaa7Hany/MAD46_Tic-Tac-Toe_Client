/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.tictactoeclient;

import static com.mycompany.tictactoeclient.Pages.PlayerComponent;
import com.mycompany.tictactoeclient.network.InvitationListener;
import com.mycompany.tictactoeshared.InvitationDTO;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.application.Platform;
/**
 * FXML Controller class
 *
 * @author LAPTOP
 */
public class LobbyPageController implements Initializable, InvitationListener {


    @FXML
    private Label myName;
    @FXML
    private Label score;
    @FXML
    private VBox playerContainer;
    
    private Platform platform;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        String [] players = {"Alaa", "Ibrahim", "Emad", "Menna","Alaa", "Ibrahim", "Emad", "Menna","Alaa", "Ibrahim", "Emad", "Menna"};
        for(String name : players){
            addPlayer(name);
        }   
    }    
    
    
    private void addPlayer(String name) {
        try {
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/com/mycompany/tictactoeclient/" + PlayerComponent + ".fxml")
            );

            AnchorPane playerComponent = loader.load();

            PlayerComponentController controller = loader.getController();
            controller.setData(name);

            playerContainer.getChildren().add(playerComponent);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void onInvitationReceived(InvitationDTO dto) {
        platform.runLater(() -> {
            //show dialog
        });
    }
    
}
