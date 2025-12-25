/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.tictactoeclient;

import static com.mycompany.tictactoeclient.Pages.PlayerComponent;
import com.mycompany.tictactoeclient.enums.SettingsPosition;
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
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
/**
 * FXML Controller class
 *
 * @author LAPTOP
 */
public class LobbyPageController implements Initializable {
    
    private PlayerDTO currentPlayer;
    //private SettingHelper settingHelper;


    @FXML
    private Label myName;
    @FXML
    private Label score;
    @FXML
    private VBox playerContainer;
    @FXML
    private StackPane rootStackPane;
//    @FXML
//    private StackPane settingLayer;
//    
//    @FXML
//    private ImageView settingIconController;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
//        settingHelper = new SettingHelper(settingLayer, SettingsPosition.BOTTOM);
//            settingIconController.setOnMouseClicked(e ->{
//                settingHelper.toggle();
//                
//            });
       
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
    
    public void setCurrentPlayer(PlayerDTO player){
        this.currentPlayer=player;
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
                
                System.out.println("##################Number of players: "+players.size());

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
    
    public void openAboutDialog() {
        try {
            App.showMyFxmlDialog(
                rootStackPane,
                "/com/mycompany/tictactoeclient/aboutDialog.fxml",
                true);
            } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
