/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tictactoeclient;

import com.mycompany.tictactoeclient.enums.Difficulty;
import com.mycompany.tictactoeclient.enums.GameMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.Region;
import com.mycompany.tictactoeclient.enums.GameResult;
import com.mycompany.tictactoeshared.TwoPlayerDTO;

/**
 *
 * @author siam
 */

public class GameHelper {
    private static final List<StepsToWin> STEPS_TO_WIN = Arrays.asList(
            new StepsToWin(1, 2, 3),
            new StepsToWin(4, 5, 6),
            new StepsToWin(7, 8, 9),
            new StepsToWin(1, 4, 7),
            new StepsToWin(2, 5, 8),
            new StepsToWin(3, 6, 9),
            new StepsToWin(1, 5, 9),
            new StepsToWin(3, 5, 7)
    );
    public static int getCellNum(int row, int col) {
        return (row * 3) + col + 1;
    }
    public static int[] getRowCol(int cellNum) {
        int row = (cellNum - 1) / 3;
        int col = (cellNum - 1) % 3;
        return new int[]{row, col};
    }
    public static boolean checkWin(List<Integer> steps) {
        for (StepsToWin s : STEPS_TO_WIN) {
            if (steps.contains(s.step1)
                    && steps.contains(s.step2)
                    && steps.contains(s.step3)) {
                return true;
            }
        }
        return false;
    }
    public static boolean checkDraw(List<Integer> xSteps, List<Integer> oSteps) {
        return (xSteps.size() + oSteps.size()) == 9;
    }
    public static List<Integer> getAvailableCells(List<Integer> xSteps, List<Integer> oSteps) {
        List<Integer> availableCells = new ArrayList<>();
        for (int i = 1; i <= 9; i++) {
            if (!xSteps.contains(i) && !oSteps.contains(i)) {
                availableCells.add(i);
            }
        }
        return availableCells;
    }
    public static StackPane findCellByNumber(GridPane gameBoard, int cellNum) {
        int[] rowCol = getRowCol(cellNum);
        int row = rowCol[0];
        int col = rowCol[1];
        for (Node node : gameBoard.getChildren()) {
            Integer nodeRow = GridPane.getRowIndex(node);
            Integer nodeCol = GridPane.getColumnIndex(node);
            int r = (nodeRow == null) ? 0 : nodeRow;
            int c = (nodeCol == null) ? 0 : nodeCol;
            if (r == row && c == col) {
                return (StackPane) node;
            }
        }
        return null;
    }
    public static void addMoveToCell(StackPane cell, boolean isXPlayer) {
        String playerLbl = isXPlayer ? "X" : "O";
        String playerLblStyle = isXPlayer ? "x-label" : "o-label";
        Label lbl = new Label(playerLbl);
        lbl.getStyleClass().add(playerLblStyle);
        cell.getChildren().add(lbl);
    }
    public static void showGameOverDialog(StackPane rootStackPane,TwoPlayerDTO towPalyer,GameMode mode, Difficulty difficulty, GameResult _gameResult, boolean isLose,int xScore,int oScore) throws java.io.IOException {
        FXMLLoader loader = new FXMLLoader(GameHelper.class.getResource(Pages.gameOverPage + ".fxml"));
        Parent dialog = loader.load();

        GameOverPageController controller = loader.getController();
        controller.initGameOver(towPalyer,mode,difficulty,_gameResult, isLose, xScore, oScore);

        Region dimmer = new Region();
        dimmer.setStyle("-fx-background-color: rgba(0, 0, 0, 0.5);");

        dimmer.prefWidthProperty().bind(rootStackPane.widthProperty());
        dimmer.prefHeightProperty().bind(rootStackPane.heightProperty());

        rootStackPane.getChildren().addAll(dimmer, dialog);
    }
    public static int getGridIndex(Integer index) {
        return (index == null) ? 0 : index;
    }
    public static class StepsToWin {
        int step1, step2, step3;
        public StepsToWin(int step1, int step2, int step3) {
            this.step1 = step1;
            this.step2 = step2;
            this.step3 = step3;
        }
    }
}
