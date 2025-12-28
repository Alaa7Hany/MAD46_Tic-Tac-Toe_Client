/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.tictactoeclient;

import com.mycompany.tictactoeclient.enums.Difficulty;
import com.mycompany.tictactoeclient.enums.GameMode;
import com.mycompany.tictactoeclient.network.NetworkConnection;
import com.mycompany.tictactoeshared.InvitationDTO;
import com.mycompany.tictactoeshared.Request;
import com.mycompany.tictactoeshared.RequestType;
import com.mycompany.tictactoeshared.Response;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

/**
 * FXML Controller class
 *
 * @author hp
 */
public class InvitationDialogController implements Initializable {

    @FXML
    private Label playerLbl;
    @FXML
    private Label scoreLbl;
    
    private InvitationDTO invitationDTO;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    public void setInvitationData(InvitationDTO dto) {
        System.out.println("setInvitationData: " + dto);
        this.invitationDTO = dto;
        playerLbl.setText(dto.getFromUsername().getUsername());
        scoreLbl.setText("Score: " + dto.getToUsername().getScore()); 
    }

    @FXML
    private void onReject(ActionEvent event) {
        try {
            
            new Thread(() -> {
                
            Request request = new Request(RequestType.REJECT_INVITE,invitationDTO);
            
            NetworkConnection.getConnection().sendMessage(request);
            
        }).start();
           
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeDialog();
        }
    }

    @FXML
    private void onAccept(ActionEvent event) {
        try {
            
        new Thread(() -> {
            Request request = new Request(RequestType.ACCEPT_INVITE,invitationDTO );
        
            NetworkConnection.getConnection().sendInvitation(request);
            
            //TODO navigate to game page with game session 
            Platform.runLater(() -> {
//                try {
//                 App.setRoot(Pages.gamePage, (GamePageController gameController) -> {
//                gameController.initGame(GameMode.ONLINE,Difficulty.EASY , 0, 0);
//            });
//                } catch (IOException ex) {
//                    System.getLogger(InvitationDialogController.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
//                }
            });
            
        }).start();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeDialog();
        }
    }
    
    private void closeDialog() {
        StackPane stack = (StackPane) playerLbl.getScene().getRoot();
        int size = stack.getChildren().size();
        if (size >= 2) {
            stack.getChildren().remove(size - 1);
        }
    }
    
}
