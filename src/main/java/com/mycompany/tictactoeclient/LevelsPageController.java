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
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

/**
 * FXML Controller class
 *
 * @author hp
 */
public class LevelsPageController implements Initializable {

    @FXML
    private ImageView back;
    @FXML
    private StackPane easyButton;
    @FXML
    private StackPane mediumButton;
    @FXML
    private StackPane hardButton;

    /**
     * Initializes the controller class.
     */
        @Override
    public void initialize(URL url, ResourceBundle rb) {
        easyButton.setOnMouseClicked(this::easyClicked);
        mediumButton.setOnMouseClicked(this::mediumClicked);
        hardButton.setOnMouseClicked(this::hardClicked);
        back.setOnMouseClicked(this::backClicked);
    }    

    @FXML
    private void backClicked(MouseEvent event) {
        try {
            App.navigateTo(Pages.startPage);  
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void easyClicked(MouseEvent event) {
        startSinglePlayerGame(Difficulty.EASY);
    }

    @FXML
    private void mediumClicked(MouseEvent event) {
        startSinglePlayerGame(Difficulty.MEDIUM);
    }

    @FXML
    private void hardClicked(MouseEvent event) {
        startSinglePlayerGame(Difficulty.HARD);
    }
    
    private void startSinglePlayerGame(Difficulty difficulty) {
        try {
            
            App.setRoot(Pages.gamePage, (GamePageController gameController) -> {
                gameController.initGame(GameMode.SINGLE_PLAYER, difficulty, 0, 0);
            });
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
