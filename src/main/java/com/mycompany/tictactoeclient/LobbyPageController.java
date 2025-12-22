/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.tictactoeclient;

import static com.mycompany.tictactoeclient.Pages.PlayerComponent;
import com.mycompany.tictactoeclient.network.InvitationListener;
import com.mycompany.tictactoeclient.network.NetworkConnection;
import com.mycompany.tictactoeshared.InvitationDTO;
import com.mycompany.tictactoeclient.network.NetworkDAO;
import com.mycompany.tictactoeshared.PlayerDTO;
import com.mycompany.tictactoeshared.Response;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.Region;
/**
 * FXML Controller class
 *
 * @author LAPTOP
 */
public class LobbyPageController implements Initializable, InvitationListener { // needs to receive player dto

    
    private PlayerDTO currentPlayer;
    public static LobbyPageController instance;
    private Parent waitingDialog;
    private Region waitingDimmer;

    @FXML
    private Label myName;
    @FXML
    private Label score;
    @FXML
    private VBox playerContainer;
    
    @FXML
    private Button sendInvite;
    
    private Platform platform;
    @FXML
    private StackPane rootStackPane;

    

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        instance=this;
        rootStackPane.getProperties().put("controller", this);
        NetworkConnection.getConnection().setInvitationListener(this);
       
    }    
    
    
    private void addPlayer(PlayerDTO player) {
        try {
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/com/mycompany/tictactoeclient/" + PlayerComponent + ".fxml")
            );

            AnchorPane playerComponent = loader.load();

            PlayerComponentController controller = loader.getController();
            controller.setData(player);

            playerContainer.getChildren().add(playerComponent);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void onInvitationReceived(InvitationDTO dto) {
        Platform.runLater(() -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mycompany/tictactoeclient/invitationDialog.fxml"));
                Parent dialogRoot = loader.load();
                InvitationDialogController controller = loader.getController();
                controller.setInvitationData(dto);

                rootStackPane.getChildren().add(dialogRoot);

            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
    
    public void setCurrentPlayer(PlayerDTO player){
        // TEMPfot testing : force Emad as current player
        this.currentPlayer = new PlayerDTO("Emad", 150, true);
        //this.currentPlayer=player;
        myName.setText(player.getUsername());
        score.setText(String.valueOf(player.getScore()));
        loadOnlinePlayers();
    }
    
    private void loadOnlinePlayers() {

        new Thread(() -> {

            Response response = NetworkDAO.getInstance().lobby();

            if (response.getStatus() == Response.Status.SUCCESS) {

                List<PlayerDTO> players =
                    (List<PlayerDTO>) response.getData();

                Platform.runLater(() -> {
                    playerContainer.getChildren().clear();

                    for (PlayerDTO p : players) {
                        if (p.getUsername()
                             .equals(currentPlayer.getUsername()))
                            continue;

                        addPlayer(p);
                    }
                });
            }

        }).start();
    }
    
    public void closeWaitingDialog() {
        
        Platform.runLater(() -> {
            if (waitingDialog != null) {
                rootStackPane.getChildren().remove(waitingDialog);
                waitingDialog = null;
            }
            if (waitingDimmer != null) {
                rootStackPane.getChildren().remove(waitingDimmer);
                waitingDimmer = null;
            }
            System.out.println("WAITING DIALOG CLOSED!");
        });
    }

    
     public void onInviteRejected() {
        closeWaitingDialog();
        App.showAlertMessage(rootStackPane,
                "Your invitation was rejected",
                false);
    }
    
    public void openInvitationDialog(PlayerDTO player) {
        try {
            App.showMyFxmlDialog(
                rootStackPane,
                "/com/mycompany/tictactoeclient/playerDetailsDialog",
                true
            );

            BorderPane dialog = (BorderPane)
                    rootStackPane.getChildren()
                    .get(rootStackPane.getChildren().size() - 1);

            PlayerDetailsDialogController controller =
                    (PlayerDetailsDialogController)
                            dialog.getProperties().get("controller");

            controller.setChallengedPlayer(player);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    public void sendInvite() {

        Platform.runLater(() -> {
            try {
                FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/mycompany/tictactoeclient/waitingDialog.fxml")
                );
                waitingDialog = loader.load();

                waitingDimmer = new Region();
                waitingDimmer.setStyle("-fx-background-color: rgba(0, 0, 0, 0.5);");
                waitingDimmer.prefWidthProperty().bind(rootStackPane.widthProperty());
                waitingDimmer.prefHeightProperty().bind(rootStackPane.heightProperty());

                rootStackPane.getChildren().addAll(waitingDimmer, waitingDialog);
                System.out.println("WAITING DIALOG SHOWN!");

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        new Thread(() -> {
            NetworkDAO.getInstance().sendInvite("Emad", "Hema", 350);
        }).start();
    }

    
       
}
