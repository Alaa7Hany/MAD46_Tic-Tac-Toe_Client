/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.tictactoeclient;

import com.mycompany.tictactoeclient.enums.Difficulty;
import com.mycompany.tictactoeclient.enums.GameMode;
import static com.mycompany.tictactoeclient.enums.GameMode.ONLINE;
import com.mycompany.tictactoeclient.enums.GameResult;
import com.mycompany.tictactoeclient.network.NetworkConnection;
import com.mycompany.tictactoeclient.network.NetworkDAO;
import com.mycompany.tictactoeshared.MoveDTO;
import com.mycompany.tictactoeshared.PlayerDTO;
import com.mycompany.tictactoeshared.Request;
import static com.mycompany.tictactoeshared.RequestType.MOVE;
import com.mycompany.tictactoeshared.StartGameDTO;
import com.mycompany.tictactoeshared.TwoPlayerDTO;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.util.Duration;
import com.mycompany.tictactoeclient.record.RecordManager;
import com.mycompany.tictactoeclient.record.model.GameRecord;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

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
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private boolean playerXRole;
    private boolean isSingle;
    private boolean isOnline;
    private boolean boardLocked = false;
    private String sessionID;
    private int oScore, xScore;
    private TwoPlayerDTO currentTwoPlayer;
    private List<Integer> xSteps = new ArrayList<>();
    private List<Integer> oSteps = new ArrayList<>();
    private RecordManager recordManager = new RecordManager();
    private boolean isRecording = false;
    
    @FXML
    private GridPane gameBoard;
    @FXML
    private Label roleLabel;
    
    

    // for records
    private ArrayList<Integer> moveHistory = new ArrayList<>();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        //remove this  put now use it for test
//        isOnline = true;
//        isSingle = false;
//        lockBoard();
//        setupNetworkListener();
    }

    public void initGame(TwoPlayerDTO towPalyer,GameMode mode, Difficulty difficulty, int xScore, int oScore) {
        this.currentGameMode = mode;
        this.currentDifficulty = difficulty;
        this.xScore = xScore;
        this.oScore = oScore;
        this.currentTwoPlayer = towPalyer;
        playerXlbl.setText(currentTwoPlayer.getPrimary().getUsername());
        playerOlbl.setText(currentTwoPlayer.getSecondry().getUsername());
        playerXScore.setText(xScore + "");
        playerOScore.setText(oScore + "");
        // in online we will accept two player models to set their names
        System.out.println("Starting game: " + mode + ", Difficulty: " + difficulty);

        if (mode == GameMode.ONLINE) {
            isOnline = true;
            isSingle = false;
            lockBoard();
            setupNetworkListener();
        } else {
            if (mode == GameMode.SINGLE_PLAYER) {
                isSingle = true;
                isOnline = false;
                playerXRole = true;
                playerOlbl.setText("Computer");
            } else {
                isSingle = false;
                isOnline = false;
                playerXRole = true;
            }
        }
        

        moveHistory.clear();
    }

    @FXML
    private void onSelectCell(MouseEvent event) {
        if (boardLocked) {
            return;
        }

        StackPane clickedCell = (StackPane) event.getSource();

        if (!clickedCell.getChildren().isEmpty()) {
            return;
        }

        Integer rowIndex = GridPane.getRowIndex(clickedCell);
        Integer colIndex = GridPane.getColumnIndex(clickedCell);
        int row = GameHelper.getGridIndex(rowIndex);
        int col = GameHelper.getGridIndex(colIndex);
        int cellNum = GameHelper.getCellNum(row, col);

        GameHelper.addMoveToCell(clickedCell, playerXRole);

        // for records
        moveHistory.add(cellNum);

        Sounds.playXOClick();

        if (isOnline) {
            lockBoard();
            roleLabel.setText("Watting...");
        }

        handlePlayerMove(cellNum);

        if (!isOnline) {
            playerXRole = !playerXRole;
        }

        if (isSingle) {
            lockBoard();
            performComputerMove();
        }
    }

    @FXML
    private void onRecord(ActionEvent event) {
        if (isRecording) {
            System.out.println("Already recording");
            return;
        }

        String difficultyValue =(currentDifficulty == null) ? "NONE" : currentDifficulty.name();
        
        String p1 = playerXlbl.getText();
        String p2 = playerOlbl.getText();
        String fileName = "game_" + System.currentTimeMillis() + ".dat";

        File directory = new File("records");

        // create directory if it doesn't exist'
        if (!directory.exists()) {
            directory.mkdir();
        }

        File file = new File(directory, fileName);

        try {
            FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(moveHistory);
            oos.close();
            fos.close();
        } catch (FileNotFoundException ex) {
            System.getLogger(GamePageController.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        } catch (IOException ex) {
            System.getLogger(GamePageController.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }

        recordManager.startRecord(
                currentGameMode.name(),
                difficultyValue,
                p1,
                p2
        );

        isRecording = true;
        System.out.println("Recording started");
    }

    @FXML
    private void onExit(ActionEvent event) {
        try {
            switch (currentGameMode) {
                case ONLINE:
                     FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/mycompany/tictactoeclient/LobbyPage.fxml"));

                    Parent root = loader.load();
                    LobbyPageController controller = loader.getController();

                    Stage stage = (Stage) rootStackPane.getScene().getWindow();
                    stage.getScene().setRoot(root);
                    controller.setCurrentPlayer(currentTwoPlayer.getPrimary());
                    break;
                default:
                    App.setRoot(Pages.startPage);
                    break;
            }
        } catch (IOException ex) {
            System.getLogger(GamePageController.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }

    private void performComputerMove() {
        PauseTransition pause = new PauseTransition(Duration.seconds(0.2));
        pause.setOnFinished(e -> makeComputerMove());
        pause.play();
    }

    private void makeComputerMove() {
        List<Integer> availableCells = GameHelper.getAvailableCells(xSteps, oSteps);
        if (availableCells.isEmpty()) {
            return;
        }
        
        if(GameHelper.checkWin(xSteps)||GameHelper.checkWin(oSteps)) return ;

        Random random = new Random();
        int selectedCellNum;

        if (currentDifficulty == Difficulty.EASY) {
            selectedCellNum = availableCells.get(new Random().nextInt(availableCells.size()));
        } else if (currentDifficulty == Difficulty.MEDIUM) {
            selectedCellNum = MinMaxAi.getBestMove(xSteps, oSteps, false);
        } else {
            selectedCellNum = MinMaxAi.getBestMove(xSteps, oSteps, true);
        }
        StackPane targetCell = GameHelper.findCellByNumber(gameBoard, selectedCellNum);

        if (targetCell != null) {
            GameHelper.addMoveToCell(targetCell, false);
            Sounds.playXOClick();
            oSteps.add(selectedCellNum);
            
            if (isRecording) {
                recordManager.recordMove("o", selectedCellNum);
            }

            // for records
            moveHistory.add(selectedCellNum);

            if (handleGameEnd(oSteps, GameResult.O_WIN, true)) {
                return;
            }

            playerXRole = true;
            unlockBoard();
        }
    }

    private void lockBoard() {
        boardLocked = true;
    }

    private void unlockBoard() {
        boardLocked = false;
    }

    private void setupNetworkListener() {
        new Thread(() -> {
            try {
                in = NetworkConnection.getConnection().getInputStream();
                if (in == null) {
                    System.out.println("Connection not established");
                    return;
                }

                while (true) {
                    Object obj = in.readObject();
                    if (obj instanceof Request) {
                        Request req = (Request) obj;
                        switch (req.getType()) {
                            case MOVE:
                                receiveMove(req.getData());
                                break;
                            case START_GAME:
                                startOnlineGame(req.getData());
                                break;
                            default:
                                break;
                        }
                        System.out.println("Received Response: " + req.getData().toString());

                        Platform.runLater(() -> {

                        });
                    }
                }
            } catch (IOException | ClassNotFoundException ex) {
                //////////////// Handling Server Disconnection, Don't touch ///////////////////
                
                System.out.println("Server connection lost during game: " + ex.getMessage());
            
                Platform.runLater(() -> {
                    try {
                        App.setRoot(Pages.startPage);

                         Alert alert = new Alert(Alert.AlertType.ERROR);
                         alert.setContentText("Connection to server lost.");
                         alert.show();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                
                ////////////////////////////////////////////////////////////////////
            }
        }).start();
    }

    private void startOnlineGame(Object _data) {
        StartGameDTO data = (StartGameDTO) _data;
        sessionID = data.sessionID;
        String symbol = data.symbol;

        playerXRole = symbol.equals("x");

        Platform.runLater(() -> {
            if (playerXRole) {
                roleLabel.setText("Your Role");
                unlockBoard();
            } else {
                roleLabel.setText("Waiting...");
            }
        });
    }

    private void receiveMove(Object _data) {
        MoveDTO data = (MoveDTO) _data;
        int cellNo = data.getCellNo();
        String symbol = data.getSymbol();
        boolean win = data.isWin();
        boolean draw = data.isDraw();

        Platform.runLater(() -> {
            StackPane targetCell = GameHelper.findCellByNumber(gameBoard, cellNo);

            if (targetCell != null) {
                boolean isXPlayer = symbol.equals("x");
                GameHelper.addMoveToCell(targetCell, isXPlayer);
                Sounds.playXOClick();

                // for records
                moveHistory.add(cellNo);

                if (isXPlayer) {
                    xSteps.add(cellNo);
                } else {
                    oSteps.add(cellNo);
                }
                
                if (isRecording) {
                    recordManager.recordMove(symbol, cellNo);
                }

                if (win) {
                    GameResult result = isXPlayer ? GameResult.X_WIN : GameResult.O_WIN;
                    showGameOverSafely(result, true, xScore, oScore);
                    return;
                }

                if (draw) {
                    showGameOverSafely(GameResult.NO_WIN, false, xScore, oScore);
                    return;
                }
                roleLabel.setText("Your Role");
                unlockBoard();
            }
        });
    }

    private void handlePlayerMove(int cellNum) {
        List<Integer> currentSteps = playerXRole ? xSteps : oSteps;
        GameResult winResult = playerXRole ? GameResult.X_WIN : GameResult.O_WIN;
        String symbol = playerXRole ? "x" : "o";

        currentSteps.add(cellNum);
        
        if(isRecording){
            recordManager.recordMove(symbol, cellNum);
            
        }
        

        if (isOnline) {
            NetworkDAO.getInstance().sendMove(sessionID, cellNum, symbol,
                    GameHelper.checkWin(currentSteps), GameHelper.checkDraw(xSteps, oSteps));
        }

        handleGameEnd(currentSteps, winResult, false);
    }

    private boolean handleGameEnd(List<Integer> steps, GameResult winResult, boolean isLose) {
        if (GameHelper.checkWin(steps)) {
            List<Integer> winningCells = GameHelper.getWinningCells(steps);
            if(isRecording){
                recordManager.finishRecord(winResult.name(), winningCells);
            }

            GameHelper.StepsToWin winLine = GameHelper.getWinningLine(steps);
            GameHelper.showWinningLine(gameBoard, winLine);
            
            if (winResult == GameResult.X_WIN) {
                    System.out.println("xxxxxxx" +xScore );
                   xScore++;
                    System.out.println("xxxxxxx" +xScore );
               } else {
                   oScore++;
               }
            
            PauseTransition delay = new PauseTransition(Duration.seconds(1));
          
            delay.setOnFinished(eh ->{
               showGameOverSafely(winResult, isLose, xScore, oScore);
            });
            delay.play();
            return true;
        }

        if (GameHelper.checkDraw(xSteps, oSteps)) {
            
            if(isRecording){
                recordManager.finishRecord("DRAW", new ArrayList<>());
            }
            
            showGameOverSafely(GameResult.NO_WIN, false, xScore, oScore);
            return true;
        }

        return false;
    }

    private void showGameOverSafely(GameResult result, boolean isLose, int _xScore, int _oScore) {
        try {
            GameHelper.showGameOverDialog(rootStackPane,currentTwoPlayer, currentGameMode, currentDifficulty, result, isLose, _xScore, _oScore);
        } catch (IOException ex) {
            System.getLogger(GamePageController.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }
}
