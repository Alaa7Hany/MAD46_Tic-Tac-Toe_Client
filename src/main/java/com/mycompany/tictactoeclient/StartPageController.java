/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.tictactoeclient;


import com.mycompany.tictactoeclient.enums.SettingsPosition;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;
import javafx.animation.AnimationTimer;
import javafx.css.Style;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.CacheHint;
import javafx.scene.Node;

import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

import javafx.scene.layout.StackPane;

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
    
    
    private Pane animationPane;
    private AnimationTimer timer;
    private final List<FloatingElement> floatingElements = new ArrayList();
    private final Random random = new Random();
    
    
    @FXML
    private StackPane settingLayer;
    
    @FXML
    private ImageView settingIconController;
    
    private SettingHelper settingHelper;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        startFloatingAnimation();

        // TODO
            rootStackPane.getProperties().put("controller", this);
        
            settingLayer.setMouseTransparent(true);
            settingHelper = new SettingHelper(settingLayer, SettingsPosition.TOP);
            settingIconController.setOnMouseClicked(e ->{
                settingHelper.toggle();
                
            });
        
    }    
    
    @FXML
    private void singlePlayerClicked(MouseEvent event) throws IOException {
        System.out.println("Single Player Button Clicked!");
        App.setRoot(Pages.levelsPage);
        
        
    }

    @FXML
    private void multiPlayerClicked(MouseEvent event) {
        try {   
            App.showMyFxmlDialog(rootStackPane, Pages.localOrOnlineDialog, true);
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
    
    
    
    ////////////////////// Animations ///////////////////////
    
    private void startFloatingAnimation(){
        animationPane = new Pane();
        animationPane.setMouseTransparent(true);
        
        rootStackPane.getChildren().add(0, animationPane);
        
        createFloatingNode("X", "floating-x");
        createFloatingNode("O", "floating-o");
        
        timer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                updateAnimation();
            }
        };
        timer.start();
        
    }
    
    private void createFloatingNode(String text, String style){
        Label label = new Label(text);
        label.getStyleClass().add(style);
        label.setOpacity(1);
        
        
        // because of the flickering
        // javafx redraws the label everytime so we need to cache it
        label.setCache(true);
        label.setCacheHint(CacheHint.QUALITY);
        
        // starting position (300, 400? to assure they spawn inside the pane )
        double startX = random.nextDouble() * 400;
        double startY = random.nextDouble() * 300;
        
        // create random directions for x and o
        double speed = 2;
        double velX = random.nextBoolean() ? speed : -speed;
        double velY = random.nextBoolean() ? speed : -speed;
        
        FloatingElement element = new FloatingElement(label, velX, velY);
        
        element.node.setLayoutX(startX);
        element.node.setLayoutY(startY);
        
        animationPane.getChildren().add(label);
        floatingElements.add(element);        
    }
    
    private void updateAnimation(){
        double width = App.WIDTH;
        double height = App.HEIGHT;
        
        if(width==0 || height ==0) return;
        
        for(FloatingElement element : floatingElements){
            double newX = element.node.getLayoutX() + element.dx;
            double newY = element.node.getLayoutY() + element.dy;
            
            double nodeWidth = element.node.getBoundsInLocal().getWidth();
            double nodeHeight = element.node.getBoundsInLocal().getHeight();
            
            if (newX <= 0 || newX + nodeWidth >= width + 30) {
                element.dx *= -1; 
            }

            if (newY <= -25 || newY + nodeHeight >= height + 55) {
                element.dy *= -1; 
            }

            element.node.setLayoutX(newX);
            element.node.setLayoutY(newY);
            
            element.node.setRotate(element.node.getRotate() + 1);
        }
    }
    
    
    private static class FloatingElement{
        Node node;
        double dx;
        double dy;

        public FloatingElement(Node node, double dx, double dy) {
            this.node = node;
            this.dx = dx;
            this.dy = dy;
        }
        
    }
    
    //////////////////////////////////////////////////////////////////


}
