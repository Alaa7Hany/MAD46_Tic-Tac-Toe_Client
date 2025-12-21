

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.tictactoeclient;

import com.mycompany.tictactoeclient.enums.Difficulty;
import com.mycompany.tictactoeclient.enums.GameMode;
import com.mycompany.tictactoeclient.enums.GameResult;
import com.mycompany.tictactoeclient.network.NetworkDAO;
import com.mycompany.tictactoeshared.MoveDTO;
import com.mycompany.tictactoeshared.Request;
import static com.mycompany.tictactoeshared.RequestType.MOVE;
import com.mycompany.tictactoeshared.Response;
import com.mycompany.tictactoeshared.StartGameDTO;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import static java.lang.System.in;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.Region;
import javafx.util.Duration;

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
    @FXML
    private GridPane gameBoard;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        playerXScore.setText("0");
        playerOScore.setText("0");
 
        //remove this  put now use it for test
        isOnline = true;
        isSingle = false;
        lockBoard();
        setupNetworkListener();
    }

    public void initGame(GameMode mode, Difficulty difficulty) {
        this.currentGameMode = mode;
        this.currentDifficulty = difficulty;

        // TODO:
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

    }

    @FXML
    private void onSelectCell(MouseEvent event) {
        if (boardLocked) {
            return;
        }
     
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

        // Sounds.playXOClick();
          if (isOnline) {
           lockBoard();
        }
        if (playerXRole) {
            xSteps.add(cellNum);
            if (isOnline) {
                  NetworkDAO.getInstance().sendMove(cellNum, "x", checkWin(xSteps), checkDraw());
        }
            if (checkWin(xSteps)) {
                try {
                    showgameOverDialog(GameResult.X_WIN, false);
                    return;
                } catch (IOException ex) {
                    System.getLogger(GamePageController.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
                }
            }
        } else {
            ySteps.add(cellNum);
                   if (isOnline) {
                  NetworkDAO.getInstance().sendMove(cellNum, "o", checkWin(ySteps), checkDraw());
        }
            if (checkWin(ySteps)) {
                try {
                    showgameOverDialog(GameResult.O_WIN, false);
                    return;
                } catch (IOException ex) {
                    System.getLogger(GamePageController.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
                }
            }

        }
        if (checkDraw()) {
            try {
                showgameOverDialog(GameResult.NO_WIN, false);
                return;

            } catch (IOException ex) {
                System.getLogger(GamePageController.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            }
        }
      if(!isOnline)  playerXRole = !playerXRole;
        if (isSingle) {
            lockBoard();
            performComputerMove();
        }
    }

    @FXML
    private void onRecord(ActionEvent event) {

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

    private void performComputerMove() {
        PauseTransition pause = new PauseTransition(Duration.seconds(0.2));
        pause.setOnFinished(e -> makeComputerMove());
        pause.play();
    }

    private void makeComputerMove() {
        List<Integer> availableCells = new ArrayList<>();
        for (int i = 1; i <= 9; i++) {
            if (!xSteps.contains(i) && !ySteps.contains(i)) {
                availableCells.add(i);
            }
        }
        if (availableCells.isEmpty()) {
            return;
        }
        Random random = new Random();
        int selectedCellNum = availableCells.get(random.nextInt(availableCells.size()));
        int row = (selectedCellNum - 1) / 3;
        int col = (selectedCellNum - 1) % 3;

        for (javafx.scene.Node node : gameBoard.getChildren()) {

            Integer nodeRow = GridPane.getRowIndex(node);
            Integer nodeCol = GridPane.getColumnIndex(node);
            int r = (nodeRow == null) ? 0 : nodeRow;
            int c = (nodeCol == null) ? 0 : nodeCol;

            if (r == row && c == col) {
                StackPane targetCell = (StackPane) node;

                Label lbl = new Label("O");
                lbl.getStyleClass().add("o-label");
                targetCell.getChildren().add(lbl);

                // Sounds.playXOClick();
                ySteps.add(selectedCellNum);

                if (checkWin(ySteps)) {

                    try {
                        showgameOverDialog(GameResult.O_WIN, true);
                        return;
                    } catch (IOException ex) {
                        System.getLogger(GamePageController.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
                    }

                }
                if (checkDraw()) {
                    try {
                        showgameOverDialog(GameResult.NO_WIN, false);
                        return;
                    } catch (IOException ex) {
                        System.getLogger(GamePageController.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
                    }
                }
                playerXRole = true;
                unlockBoard();
                break;
            }

        }
    }

    private boolean checkDraw() {
        return (xSteps.size() + ySteps.size()) == 9;
    }

    private void lockBoard() {
        boardLocked = true;
    }

    private void unlockBoard() {
        boardLocked = false;
    }

    private void showgameOverDialog(GameResult _gameResult, boolean isLose) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource(Pages.gameOverPage + ".fxml"));
        Parent dialog = loader.load();

        GameOverPageController controller = loader.getController();
        controller.initGameOver(_gameResult, isLose);

        Region dimmer = new Region();
        dimmer.setStyle("-fx-background-color: rgba(0, 0, 0, 0.5);");

        dimmer.prefWidthProperty().bind(rootStackPane.widthProperty());
        dimmer.prefHeightProperty().bind(rootStackPane.heightProperty());

        rootStackPane.getChildren().addAll(dimmer, dialog);
    }

    private void setupNetworkListener() {
        new Thread(() -> {
            try {
                in = com.mycompany.tictactoeclient.network.NetworkConnection.getConnection().getInputStream();
                if (in == null) {
                    System.out.println("Connection not established");
                    return;
                }

                while (true) {
                    Object obj = in.readObject(); // wait any response
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
                ex.printStackTrace();
            }
        }).start();
    }

    private void startOnlineGame(Object _data) {
        StartGameDTO data = (StartGameDTO) _data;
        String symbol = data.symbol;
        playerXRole = symbol.equals("x") ? true : false;
        if (playerXRole) {
          System.out.println("unlooc");
            unlockBoard();
        }

    }
    
  
   private void  receiveMove(Object _data){
         MoveDTO data = (MoveDTO) _data;
            int cellNo = data.getCellNo();
            String symbol = data.getSymbol();
            boolean win = data.isWin();
            boolean draw = data.isDraw();
       Platform.runLater(() -> {
           int row = (cellNo - 1) / 3;
           int col = (cellNo - 1) % 3;

           for (Node node : gameBoard.getChildren()) {

               Integer nodeRow = GridPane.getRowIndex(node);
               Integer nodeCol = GridPane.getColumnIndex(node);
               int r = (nodeRow == null) ? 0 : nodeRow;
               int c = (nodeCol == null) ? 0 : nodeCol;

               if (r == row && c == col) {
                   StackPane targetCell = (StackPane) node;

                   Label lbl = new Label(symbol.toUpperCase());
                   String playerLblStyle = symbol.equals("x") ? "x-label" : "o-label";
                   lbl.getStyleClass().add(playerLblStyle);
                   targetCell.getChildren().add(lbl);

                   // Sounds.playXOClick();
                   if (symbol.equals("x")) {
                       xSteps.add(cellNo);
                   } else {
                       ySteps.add(cellNo);
                   }

                   if (win) {
                       try {
                           GameResult result = symbol.equals("x") ? GameResult.X_WIN : GameResult.O_WIN;
                           showgameOverDialog(result, false);
                           return;
                       } catch (IOException ex) {
                           System.getLogger(GamePageController.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
                       }

                   }
                   if (draw) {
                       try {
                           showgameOverDialog(GameResult.NO_WIN, false);
                           return;
                       } catch (IOException ex) {
                           System.getLogger(GamePageController.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
                       }
                   }
                   unlockBoard();
                   break;
               }

           }
       });
   }
}


