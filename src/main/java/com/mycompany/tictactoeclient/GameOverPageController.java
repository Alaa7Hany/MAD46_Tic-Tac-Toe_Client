/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.tictactoeclient;

import static com.mycompany.tictactoeclient.Pages.waitingDialog;
import com.mycompany.tictactoeclient.enums.Difficulty;
import com.mycompany.tictactoeclient.enums.GameMode;
import static com.mycompany.tictactoeclient.enums.GameMode.ONLINE;
import com.mycompany.tictactoeclient.enums.GameResult;
import static com.mycompany.tictactoeclient.enums.GameResult.NO_WIN;
import static com.mycompany.tictactoeclient.enums.GameResult.O_WIN;
import static com.mycompany.tictactoeclient.enums.GameResult.X_WIN;
import com.mycompany.tictactoeclient.network.NetworkConnection;
import com.mycompany.tictactoeshared.InvitationDTO;
import com.mycompany.tictactoeshared.RematchDTO;
import com.mycompany.tictactoeshared.Request;
import com.mycompany.tictactoeshared.RequestType;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
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
    private InvitationDTO currentTwoPlayer;
    private String currentSessionId;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    public void initGameOver(String _currentSessionId, InvitationDTO towPalyer, GameMode mode, Difficulty difficulty, GameResult _gameResult, boolean isLose, int xScore, int oScore) {
        this.currentSessionId = _currentSessionId;
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
        mediaPlayer.setAutoPlay(false);
        mediaView.setPreserveRatio(true);
        mediaPlayer.setOnReady(() -> {
            mediaPlayer.play();
        });

        mediaPlayer.setOnError(() -> {
            System.out.println("Media Error: " + mediaPlayer.getError().getMessage());
        });

        mediaView.sceneProperty().addListener((observable, oldScene, newScene) -> {
            if (newScene == null && mediaPlayer != null) {
                mediaPlayer.stop();
            }
        });
    }

    @FXML
    private void reMatchAction(ActionEvent event) {
        try {
            switch (currentGameMode) {
                case ONLINE:
                    reMatchOnlineGame();
                    break;
                default:
                    SoundManager.applyState();
                    App.setRoot(Pages.gamePage, (GamePageController controller) -> {
                        controller.initGame(currentTwoPlayer, currentGameMode, currentDifficulty, xScore, oScore);
                    });
                    break;
            }

        } catch (IOException ex) {
            System.getLogger(GameOverPageController.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }

    @FXML
    private void exitAction(ActionEvent event) {
        stopMediaPlayer();
        if (GamePageController.instance != null) {
            GamePageController.instance.stopListening();
        }

        SoundManager.applyState();
        try {
            switch (currentGameMode) {
                case ONLINE:
                    Platform.runLater(() -> {
                        try {
                            FXMLLoader loader = new FXMLLoader(
                                    getClass().getResource("/com/mycompany/tictactoeclient/lobbyPage.fxml"));

                            Parent root = loader.load();
                            LobbyPageController controller = loader.getController();

                            Stage stage = (Stage) exitBtn.getScene().getWindow();
                            stage.getScene().setRoot(root);
                            controller.setCurrentPlayer(currentTwoPlayer.getFromUsername());
                        } catch (IOException ex) {
                            System.getLogger(GameOverPageController.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
                        }
                    });
                    break;
                default:
                    App.setRoot(Pages.startPage);
                    break;
            }
            if (currentGameMode == GameMode.ONLINE && GamePageController.instance != null) {
                GamePageController.instance.syncPlayerScores();
            }
            SoundManager.applyState();

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
            mediaPlayer = null;
        }
    }

    private void reMatchOnlineGame() {

        new Thread(() -> {
            RematchDTO dto = new RematchDTO(
                    currentSessionId,
                    currentTwoPlayer.getFromUsername().getUsername()
            );

            NetworkConnection.getConnection().sendMessage(
                    new Request(RequestType.REMATCH_REQUEST, dto)
            );

        }).start();

        Platform.runLater(() -> {

            Parent sceneRoot = rematchBtn.getScene().getRoot();
            if (!(sceneRoot instanceof StackPane)) {

                return;
            }
            StackPane gamePageRoot = (StackPane) sceneRoot;

            // Remove dimmer and dialog
            int size = gamePageRoot.getChildren().size();
            if (size >= 2) {
                gamePageRoot.getChildren().remove(size - 2, size); // Remove dimmer and dialog
            }

            // Add waiting dialog with dimmer
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mycompany/tictactoeclient/waitingDialog.fxml"));
                Parent waitingDialog = loader.load();

                WaitingDialogController waitingController = loader.getController();
                waitingController.setPlayerName(currentTwoPlayer.getToUsername().getUsername());

                Region dimmer = new Region();
                dimmer.setId("waitingDialogDimmer");
                dimmer.setStyle("-fx-background-color: rgba(0, 0, 0, 0.5);");
                dimmer.prefWidthProperty().bind(gamePageRoot.widthProperty());
                dimmer.prefHeightProperty().bind(gamePageRoot.heightProperty());

                gamePageRoot.getChildren().addAll(dimmer, waitingDialog);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        System.out.println("Rematch request sent");
    }
}
