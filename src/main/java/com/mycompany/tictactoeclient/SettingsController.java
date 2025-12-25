/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.tictactoeclient;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
/**
 * FXML Controller class
 *
 * @author LAPTOP
 */
public class SettingsController implements Initializable {
    private Image muteImg;
    private Image unmuteImg;
    private boolean isMuted = false;


    @FXML
    private VBox settingsRoot;
    @FXML
    private Button muteBtn;
    @FXML
    private ImageView muteIcon;
    @FXML
    private Button logoutBtn;
    @FXML
    private Button aboutBtn;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        muteImg = new Image(getClass().getResourceAsStream("/images/mute_icon.png"));
        unmuteImg = new Image(getClass().getResourceAsStream("/images/unmute-icon.png"));
        updateMuteUI();
    }    
    
    @FXML
    private void muteMusic(ActionEvent event) {

        isMuted = !isMuted;

        if (isMuted) {
            Sounds.pauseSound();
        } else {
            Sounds.resumeSound();
        }

        updateMuteUI();
    }

    private void updateMuteUI() {
        if (isMuted) {
            muteBtn.setText("Unmute");
            muteIcon.setImage(unmuteImg);
        } else {
            muteBtn.setText("Mute");
            muteIcon.setImage(muteImg);
        }
    }

    @FXML
    private void logout(ActionEvent event) {
    }

    @FXML
    private void aboutGame(ActionEvent event) {
        StackPane rootStackPane = (StackPane)settingsRoot.getScene().getRoot();
        
        try {
            App.showMyFxmlDialog(rootStackPane, "aboutDialog", isMuted);
        } catch (IOException ex) {
            System.getLogger(SettingsController.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }
    
    public void hideLogout(){
        logoutBtn.setManaged(false);
        logoutBtn.setVisible(false);
    }

}
