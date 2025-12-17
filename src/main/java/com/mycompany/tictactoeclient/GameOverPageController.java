/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.tictactoeclient;

import com.mycompany.tictactoeclient.enums.Difficulty;
import com.mycompany.tictactoeclient.enums.GameMode;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

/**
 * FXML Controller class
 *
 * @author LAPTOP
 */
public class GameOverPageController implements Initializable {

    @FXML
    private Button rematchBtn;
    @FXML
    private Button exitBtn;
    
     @FXML
    private MediaView mediaView;

    private MediaPlayer mediaPlayer;
    


    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Sounds.pauseSound();
        String path = getClass()
                .getResource("/videos/winnerVideo.mp4")
                .toExternalForm();

        Media media = new Media(path);
        mediaPlayer = new MediaPlayer(media);

        mediaView.setMediaPlayer(mediaPlayer);

        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE); 
        mediaPlayer.setAutoPlay(true); 
        
        // Stop the video when the dialog is removed from the screen
        mediaView.sceneProperty().addListener((observable, oldScene, newScene) -> {
            if (newScene == null && mediaPlayer != null) {
                mediaPlayer.stop();
            }
        });
    }    

    @FXML
    private void reMatchAction(ActionEvent event) {
        Sounds.playUiClick();
        try {
            Sounds.resumeSound();
            
            App.setRoot(Pages.gamePage, (GamePageController controller)->{
                controller.initGame(GameMode.ONLINE, Difficulty.MEDIUM);
            });
        } catch (IOException ex) {
            System.getLogger(GameOverPageController.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }

    @FXML
    private void exitAction(ActionEvent event) {
        Sounds.playUiClick();
        try {
            Sounds.resumeSound();
            App.setRoot(Pages.startPage);
        } catch (IOException ex) {
            System.getLogger(GameOverPageController.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        
    }
    
    
    
}