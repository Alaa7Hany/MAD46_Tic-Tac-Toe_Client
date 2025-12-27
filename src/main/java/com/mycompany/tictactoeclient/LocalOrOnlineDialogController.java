/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.tictactoeclient;

import com.mycompany.tictactoeclient.enums.GameMode;
import com.mycompany.tictactoeshared.InvitationDTO;
import com.mycompany.tictactoeshared.PlayerDTO;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;

import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
/**
 * FXML Controller class
 *
 * @author siam
 */
public class LocalOrOnlineDialogController implements Initializable {


    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    @FXML
    private void localClicked(MouseEvent event) {
          PlayerDTO primary =   new PlayerDTO("Player 1 ", 0, false);
            PlayerDTO secondy =   new PlayerDTO("Player 2", 0, false);
          InvitationDTO twoPlayer = new InvitationDTO(
                    primary,
                    secondy
            );
        try {
            App.setRoot(Pages.gamePage, (GamePageController controller) -> {
                controller.initGame(twoPlayer,GameMode.LOCAL_MULTIPLAYER, null,0,0);
            }); 
        } catch (IOException ex) {
            System.getLogger(LocalOrOnlineDialogController.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        
    }

    @FXML
    private void onlineClicked(MouseEvent event) {
        try {
            StackPane root = (StackPane) ((Node) event.getSource()).getScene().getRoot();
            App.showMyFxmlDialog(root, Pages.serverConfigureDialog, true);
        } catch (IOException ex) {
            System.getLogger(LocalOrOnlineDialogController.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }

}
