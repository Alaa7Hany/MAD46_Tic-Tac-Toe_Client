/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.tictactoeclient;

import com.mycompany.tictactoeclient.enums.Difficulty;
import com.mycompany.tictactoeclient.enums.GameMode;
import static com.mycompany.tictactoeclient.enums.GameMode.ONLINE;
import com.mycompany.tictactoeclient.enums.GameResult;
import static com.mycompany.tictactoeclient.enums.GameResult.NO_WIN;
import static com.mycompany.tictactoeclient.enums.GameResult.O_WIN;
import static com.mycompany.tictactoeclient.enums.GameResult.X_WIN;
import com.mycompany.tictactoeshared.TwoPlayerDTO;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;

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
    @FXML
    private Label title;

    private MediaPlayer mediaPlayer;
    private GameResult gameResult;
    private String path;
    private int oScore, xScore;
    private GameMode currentGameMode;
    private Difficulty currentDifficulty;
    private TwoPlayerDTO currentTwoPlayer;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Sounds.pauseSound();
    }

    public void initGameOver(TwoPlayerDTO towPalyer,GameMode mode, Difficulty difficulty, GameResult _gameResult, boolean isLose, int xScore, int oScore) {
        this.gameResult = _gameResult;
        this.oScore = oScore;
        this.xScore = xScore;
        this.currentGameMode = mode;
        this.currentDifficulty = difficulty;
        this.currentTwoPlayer = towPalyer;
        switch (gameResult) {
            case NO_WIN:
                title.setText("No Winner");
                path = getVideoPath("no_win");
                break;
            case O_WIN:
                title.setText("Player O Wins");
                path = getVideoPath(isLose ? "loser" : "winner");
                break;
            case X_WIN:
                title.setText("Player X Wins");
                path = getVideoPath(isLose ? "loser" : "winner");
                break;
            default:
                break;
        }

        Media media = new Media(path);
        mediaPlayer = new MediaPlayer(media);
        mediaView.setMediaPlayer(mediaPlayer);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        mediaPlayer.setAutoPlay(true);

        mediaView.sceneProperty().addListener((observable, oldScene, newScene) -> {
            if (newScene == null && mediaPlayer != null) {
                mediaPlayer.stop();
            }
        });
    }

    @FXML
    private void reMatchAction(ActionEvent event) {
        Sounds.playUiClick();
        stopMediaPlayer();
        try {
            Sounds.resumeSound();
            switch (currentGameMode) {
                case ONLINE:
                   
                    break;
                default:
                    App.setRoot(Pages.gamePage, (GamePageController controller) -> {
                        controller.initGame(currentTwoPlayer,currentGameMode, currentDifficulty, xScore, oScore);
                    });
                    break;
            }

        } catch (IOException ex) {
            System.getLogger(GameOverPageController.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }

    @FXML
    private void exitAction(ActionEvent event) {
        Sounds.playUiClick();
        stopMediaPlayer();
        try {
            Sounds.resumeSound();
            switch (currentGameMode) {
                case ONLINE:
                   FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/mycompany/tictactoeclient/LobbyPage.fxml"));

                    Parent root = loader.load();
                    LobbyPageController controller = loader.getController();

                    Stage stage = (Stage) exitBtn.getScene().getWindow();
                    stage.getScene().setRoot(root);
                    controller.setCurrentPlayer(currentTwoPlayer.getPrimary());
                    break;
                default:
                    App.setRoot(Pages.startPage);
                    break;
            }
        } catch (IOException ex) {
            System.getLogger(GameOverPageController.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }

    private String getVideoPath(String name) {
        return getClass().getResource("/videos/" + name + ".mp4").toExternalForm();
    }

    private void stopMediaPlayer() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.dispose();
        }
    }
}
