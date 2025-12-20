/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.tictactoeclient;

import com.mycompany.tictactoeclient.network.NetworkDAO;
import com.mycompany.tictactoeshared.Response;
import com.mycompany.tictactoeshared.PlayerDTO;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

/**
 * FXML Controller class
 *
 * @author hp
 */
public class PlayerDetailsDialogController implements Initializable {
    private PlayerDTO challengedPlayer;

    @FXML
    private Label nameLbl;
    @FXML
    private Label scoreLbl;
    
    private String currentUserName = "player1";  
    private String targetUserName = "player2";    
    private int targetScore = 51;    
    @FXML
    private BorderPane root;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        root.getProperties().put("controller", this);
        // TODO
    }    

    @FXML
    private void onExitClicked(ActionEvent event) {
    }

    @FXML
    private void onInviteClicked(ActionEvent event) {
        
        Response response = NetworkDAO.getInstance()
                .sendInvite(currentUserName, targetUserName,targetScore); // ths will fail cz there is no real data 
        
        if (response.getStatus() == Response.Status.SUCCESS) {
            System.out.println("Invite sent to " + targetUserName);
        } else {
            System.out.println("Failed to send invite");
        }
        
    }
    
    
     public void setChallengedPlayer(PlayerDTO player) {
        this.challengedPlayer = player;
        nameLbl.setText(player.getUsername());
        scoreLbl.setText(String.valueOf(player.getScore()));
    }
}
