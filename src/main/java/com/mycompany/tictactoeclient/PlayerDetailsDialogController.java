/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.tictactoeclient;

import com.mycompany.tictactoeclient.network.NetworkConnection;
import com.mycompany.tictactoeshared.InvitationDTO;
import com.mycompany.tictactoeshared.PlayerDTO;
import com.mycompany.tictactoeshared.Request;
import com.mycompany.tictactoeshared.RequestType;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;

/**
 * FXML Controller class
 *
 * @author hp
 */
public class PlayerDetailsDialogController implements Initializable {
    private PlayerDTO challengedPlayer;
    private PlayerDTO currentPlayer;

    @FXML
    private Label nameLbl;
    @FXML
    private Label scoreLbl;
     
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
    private void onInviteClicked(ActionEvent event) {
        
       new Thread(() -> {
        NetworkConnection.getConnection().sendInvitation(  
            new Request(RequestType.INVITE_PLAYER,new InvitationDTO(currentPlayer,challengedPlayer))
            );
        }).start();
       
       Platform.runLater(() -> {
            StackPane lobbyRoot = (StackPane) root.getScene().getRoot();
        
            lobbyRoot.getChildren().remove(root.getParent());
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mycompany/tictactoeclient/waitingDialog.fxml"));
                Parent waitingDialog = loader.load();
                Region dimmer = new Region();
                dimmer.setStyle("-fx-background-color: rgba(0, 0, 0, 0.5);");
                dimmer.prefWidthProperty().bind(root.widthProperty());
                dimmer.prefHeightProperty().bind(root.heightProperty());

                lobbyRoot.getChildren().addAll(dimmer, waitingDialog);
                LobbyPageController.instance.setWaitingDialog(waitingDialog, dimmer);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        System.out.println("Inivte sent");
    }
    
    
     public void setChallengedPlayer(PlayerDTO player) {
        this.challengedPlayer = player;
        nameLbl.setText(player.getUsername());
        scoreLbl.setText(String.valueOf(player.getScore()));
    }
     
     public void setCurrentPlayer(PlayerDTO player) {
        this.currentPlayer = player;
    }
}
