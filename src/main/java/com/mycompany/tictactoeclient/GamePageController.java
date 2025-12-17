/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.tictactoeclient;

import com.mycompany.tictactoeclient.enums.Difficulty;
import com.mycompany.tictactoeclient.enums.GameMode;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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

   
    private boolean playerXRole;

    List<Integer> xSteps = new ArrayList<>();
    List<Integer> ySteps = new ArrayList<>();
    List<StepsToWin> stepsToWin = Arrays.asList(
            new StepsToWin(1, 2, 3),
            new StepsToWin(4, 5, 6),
            new StepsToWin(7, 8, 9),
            new StepsToWin(1, 4, 7),
            new StepsToWin(2, 5, 8),
            new StepsToWin(3, 6, 9),
            new StepsToWin(1, 5, 9),
            new StepsToWin(3, 5, 7)
    );

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       playerXScore.setText("0");
       playerOScore.setText("0");
        playerXRole = true;
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

        String playerLbl = playerXRole ? "X" : "O";
        String playerLblStyle = playerXRole ? "x-label" : "o-label";
        Label lbl = new Label(playerLbl);
        lbl.getStyleClass().add(playerLblStyle);

        // add the X or O to the screen
        clickedCell.getChildren().add(lbl);

        Sounds.playXOClick();
        if (playerXRole) {
            xSteps.add(cellNum);
            if (checkWin(xSteps)) {
                try {
                    App.showMyFxmlDialog(rootStackPane, Pages.gameOverPage, false);
                } catch (IOException ex) {
                    System.getLogger(GamePageController.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
                }
            }
        } else {
            ySteps.add(cellNum);
            if (checkWin(ySteps)) {
                System.out.println("O wins!");
            }
             
        }

        playerXRole = !playerXRole;
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

    private int getCellNum(int row, int col) {
        // return a number from 1 to 9
        return (row * 3) + col + 1;
    }

    private boolean checkWin(List<Integer> steps) {
        for (StepsToWin s : stepsToWin) {
            if (steps.contains(s.step1)
                    && steps.contains(s.step2)
                    && steps.contains(s.step3)) {
                return true;
            }
        }
        return false;
    }

    public class StepsToWin {

        int step1, step2, step3;

        public StepsToWin(int step1, int step2, int step3) {
            this.step1 = step1;
            this.step2 = step2;
            this.step3 = step3;
        }
    }

}
