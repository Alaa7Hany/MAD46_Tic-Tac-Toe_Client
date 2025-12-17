/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.tictactoeclient;

import com.mycompany.tictactoeclient.enums.Difficulty;
import com.mycompany.tictactoeclient.enums.GameMode;
import com.mycompany.tictactoeshared.LoginDTO;
import com.mycompany.tictactoeshared.MoveDTO;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
/**
 * FXML Controller class
 *
 * @author siam
 */
public class StartPageController implements Initializable {


    @FXML
    private StackPane singlePlayerButton;
    @FXML
    private StackPane multiPalyerButton;
    @FXML
    private StackPane rootStackPane;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    @FXML
    private void singlePlayerClicked(MouseEvent event) throws IOException {
        System.out.println("Single Player Button Clicked!");
        App.setRoot("gamePage", (GamePageController controller) -> {
            controller.initGame(GameMode.SINGLE_PLAYER, Difficulty.EASY);
        });
        
        
    }

    @FXML
    private void multiPlayerClicked(MouseEvent event) {
        try {   
            App.showMyFxmlDialog(rootStackPane, Pages.localOrOnlineDialog, false);
        } catch (IOException ex) {
            System.getLogger(StartPageController.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
       
        
    }

    @FXML
    private void onRecordsClicked(ActionEvent event) {
        try {
            App.setRoot(Pages.recordsPage);
        } catch (IOException ex) {
            System.getLogger(StartPageController.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        
    }



}
