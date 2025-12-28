package com.mycompany.tictactoeclient;

import com.mycompany.tictactoeclient.record.RecordReader;
import com.mycompany.tictactoeclient.record.ReplayMove;
import com.mycompany.tictactoeclient.enums.GameResult;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.InnerShadow;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class RecordReplayPageController implements Initializable {

    @FXML
    private GridPane gameBoard;
    @FXML
    private StackPane rootStackPane;
    @FXML
    private Label playerXlbl;
    @FXML
    private Label playerOlbl;
    @FXML
    private Label playerXScore;
    @FXML
    private Label playerOScore;
    @FXML
    private Label roleLabel;
    @FXML
    private StackPane settingLayer;
    @FXML
    private javafx.scene.control.Button backButton;

    private List<ReplayMove> moves;
    private int currentMove = 0;

    private List<Integer> xSteps = new ArrayList<>();
    private List<Integer> oSteps = new ArrayList<>();

    private String playerXName = "Player X";
    private String playerOName = "Player O";

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        gameBoard.setDisable(true);
    }

    public void setPlayersNames(String xName, String oName) {
        this.playerXName = xName;
        this.playerOName = oName;
        playerXlbl.setText(playerXName);
        playerOlbl.setText(playerOName);
    }

    public void loadReplay(File recordFile) {
        moves = RecordReader.readMoves(recordFile);

        String[] players = RecordReader.readPlayers(recordFile);
            if (players != null && players.length == 2) {
                setPlayersNames(players[0], players[1]);
            }

        xSteps.clear();
        oSteps.clear();
        currentMove = 0;
        clearBoard();
        playReplay();
    }

    private void playReplay() {
        if (moves == null || moves.isEmpty()) return;

        Timeline timeline = new Timeline();

        for (int i = 0; i < moves.size(); i++) {
            ReplayMove move = moves.get(i);
            ReplayMove moveCopy = move;
            KeyFrame frame = new KeyFrame(Duration.seconds(0.5 * (i + 1)), e -> drawMove(moveCopy));
            timeline.getKeyFrames().add(frame);
        }

        timeline.setOnFinished(e -> checkWinner());
        timeline.play();
    }

    private void drawMove(ReplayMove move) {
        int cellNo = move.getCellNo();
        boolean isXPlayer = move.getSymbol().equalsIgnoreCase("X");

        StackPane cell = GameHelper.findCellByNumber(gameBoard, cellNo);
        if (cell == null) return;

        cell.getChildren().clear();

        Text txt = new Text(isXPlayer ? "X" : "O");
        txt.setFont(playerXlbl.getFont());
        txt.setFont(playerXlbl.getFont().font(playerXlbl.getFont().getFamily(), 50));
        txt.setStyle(
            "-fx-font-weight: bold;" 
        );

        txt.setFill(Color.web(isXPlayer ? "#F5373A" : "#D7EA91"));

        InnerShadow innerShadow = new InnerShadow();
        innerShadow.setRadius(1.5);
        innerShadow.setColor(Color.rgb(0, 0, 0, 0.5));
        txt.setEffect(innerShadow);

        cell.getChildren().add(txt);

        if (isXPlayer) xSteps.add(cellNo);
        else oSteps.add(cellNo);
    }

    private void clearBoard() {
        for (var node : gameBoard.getChildren()) {
            if (node instanceof StackPane) {
                StackPane cell = (StackPane) node;
                cell.getChildren().clear();
                cell.setStyle("-fx-background-color: #B3ACCA; -fx-border-color: #000000;");
            }
        }
    }

    private void checkWinner() {
        GameResult result = null;
        List<Integer> winningSteps = null;

        if (GameHelper.checkWin(xSteps)) {
            result = GameResult.X_WIN;
            winningSteps = xSteps;
        } else if (GameHelper.checkWin(oSteps)) {
            result = GameResult.O_WIN;
            winningSteps = oSteps;
        } else {
            result = GameResult.NO_WIN;
        }

        if (winningSteps != null && !winningSteps.isEmpty()) {
            List<Integer> winningCells = GameHelper.getWinningCells(winningSteps);
            GameHelper.StepsToWin winLine = GameHelper.getWinningLine(winningCells);
            GameHelper.showWinningLine(gameBoard, winLine);
        }
    }

    @FXML
    private void onBack(javafx.event.ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/mycompany/tictactoeclient/recordsPage.fxml")
            );

            Pane newContent = loader.load();
            Pane currentRoot = (Pane) backButton.getScene().getRoot();
            currentRoot.getChildren().setAll(newContent);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}