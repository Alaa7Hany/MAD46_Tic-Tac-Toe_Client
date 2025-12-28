/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.tictactoeclient;

import animations.BackgroundAnimator;
import static com.mycompany.tictactoeclient.Pages.PlayerComponent;
import com.mycompany.tictactoeclient.enums.SettingsPosition;
import com.mycompany.tictactoeclient.network.InvitationListener;
import com.mycompany.tictactoeclient.network.LobbyListener;
import com.mycompany.tictactoeclient.network.NetworkConnection;
import com.mycompany.tictactoeclient.network.NetworkDAO;
import com.mycompany.tictactoeshared.InvitationDTO;
import com.mycompany.tictactoeshared.PlayerDTO;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
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
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
/**
 * FXML Controller class
 *
 * @author LAPTOP
 */
public class LobbyPageController implements Initializable, InvitationListener, LobbyListener { 

    
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
    
    private SettingHelper settingHelper;
    private Platform platform;
    @FXML
    private StackPane rootStackPane;
    @FXML
    private StackPane settingLayer;
    
    @FXML
    private ImageView settingIconController;

    

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        instance=this;
        NetworkConnection.reConnectListener();
        rootStackPane.getProperties().put("controller", this);
        NetworkConnection.getConnection().setInvitationListener(this);
        NetworkConnection.getConnection().setLobbyListener(this);
        
        new BackgroundAnimator(rootStackPane);
        
        settingHelper = new SettingHelper(settingLayer, SettingsPosition.BOTTOM);
            settingIconController.setOnMouseClicked(e ->{
                settingHelper.toggle();
                
            });
        
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
        //his.currentPlayer = new PlayerDTO("Emad", 150, true);
        this.currentPlayer=player;
        myName.setText(player.getUsername());
        score.setText(String.valueOf(player.getScore()));
        loadOnlinePlayers();
    }
    
    private void loadOnlinePlayers() {
            NetworkConnection.getConnection().startLobbyListener();
            NetworkDAO.getInstance().requestOnlinePlayers();
    }
    
        
    public void setWaitingDialog(Parent dialog, Region dimmer) {
        this.waitingDialog = dialog;
        this.waitingDimmer = dimmer;
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
            controller.setCurrentPlayer(currentPlayer);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    
    // will remove it 
    public void sendInvite() {

        /*Platform.runLater(() -> {
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
        NetworkConnection.getConnection().sendMessage(  
            new Request(RequestType.INVITE_PLAYER,new InvitationDTO(currentPlayer,player))
            );
        }).start();*/
    }

    @Override
    public void onOnlinePlayersUpdated(List<PlayerDTO> players) {
        System.out.println("New player list received: " + players.size());
        
        playerContainer.getChildren().clear();
        
        for(PlayerDTO player : players){
            if(player.getUsername().equals(currentPlayer.getUsername())) continue;
            addPlayer(player);
            
        }
    }

    
       
}