/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.tictactoeclient;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

/**
 * FXML Controller class
 *
 * @author hp
 */
public class GamePageController implements Initializable {

    @FXML
    private Label playerXlbl;
    @FXML
    private Label playerXScore;
    @FXML
    private Label playerOlbl;
    @FXML
    private Label playerOScore;
    
    private String gameMode; // e.g., "Single", "Local", "Online"
    private String difficulty; // e.g., "Easy", "Medium", "Hard" (only for Single)

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    public void initGame(String mode, String difficulty) {
        this.gameMode = mode;
        this.difficulty = difficulty;
        
        System.out.println("Starting game: " + mode + ", Difficulty: " + difficulty);
        
        // Setup logic based on mode (e.g., enable AI if "Single")
        if ("Single".equals(mode)) {
            // Setup AI player
            playerOlbl.setText("Computer");
        } else {
            // Setup multiplayer names
        }
    }    

    @FXML
    private void onCellSelected(MouseEvent event) {
    }

    @FXML
    private void onSelectCell(MouseEvent event) {
    }

    @FXML
    private void onRecord(ActionEvent event) {
    }

    @FXML
    private void onExit(ActionEvent event) {
    }
    
}
