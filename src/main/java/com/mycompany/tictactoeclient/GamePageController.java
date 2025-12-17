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
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

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
    
    private GameMode currentGameMode;
    private Difficulty currentDifficulty;
    @FXML
    private StackPane rootStackPane;
  
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    public void initGame(GameMode mode, Difficulty difficulty) {
        this.currentGameMode = mode;
        this.currentDifficulty = difficulty;
        
        // TODO:
        // in online we will accept two player models to set their names
        
        System.out.println("Starting game: " + mode + ", Difficulty: " + difficulty);
        
        // Setup logic based on mode (e.g., enable AI if "Single")
        if (mode == GameMode.SINGLE_PLAYER) {
            // Setup AI player
            playerOlbl.setText("Computer");
        } else {
            // Setup multiplayer names
        }
    }    


    @FXML
    private void onSelectCell(MouseEvent event) {
        // get the cell that was clicked
        StackPane clickedCell = (StackPane) event.getSource();

        // prevent modiying an already modified cell
        if (!clickedCell.getChildren().isEmpty()) {
            return; 
        }
        
        // get the number of the cell
        Integer rowIndex = GridPane.getRowIndex(clickedCell);
        Integer colIndex = GridPane.getColumnIndex(clickedCell);
        int row = (rowIndex == null) ? 0 : rowIndex;
        int col = (colIndex == null) ? 0 : colIndex;
        int cellNum = getCellNum(row, col);
        System.out.println("Clicked Cell: " + cellNum);
        
        
        Label lbl = new Label("X");
        // get the style from styles.css for X or O
        lbl.getStyleClass().add("x-label");
        
        // add the X or O to the screen
        clickedCell.getChildren().add(lbl);
        
        Sounds.playXOClick();
          
    }

    @FXML
    private void onRecord(ActionEvent event) {
        try {
            ////////////////////////////////
            ////
            /// For now i will use it to try out the gamoOverPage
            ///
            ///////////////////////
            ///
            App.showMyFxmlDialog(rootStackPane, Pages.gameOverPage, false);
            
        } catch (IOException ex) {
            System.getLogger(GamePageController.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }

    @FXML
    private void onExit(ActionEvent event) {
    }
    
    
    private int getCellNum(int row, int col){
        // return a number from 1 to 9
        return (row * 3) + col + 1;
    }
}
