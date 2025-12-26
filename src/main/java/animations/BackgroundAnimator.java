/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package animations;

import com.mycompany.tictactoeclient.App;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javafx.animation.AnimationTimer;
import javafx.scene.CacheHint;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

/**
 *
 * @author hp
 */
public class BackgroundAnimator {

    private final Pane animationPane;
    private final AnimationTimer timer;
    private final List<FloatingElement> floatingElements = new ArrayList<>();
    private final Random random = new Random();
    
    public BackgroundAnimator(StackPane rootStackPane) {
        animationPane = new Pane();
        animationPane.setMouseTransparent(true);
        
        // Add at index 0 as specified in your StartPageController
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

        // Stop animation when the view is removed from the scene
        animationPane.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene == null) {
                timer.stop();
            } else {
                timer.start();
            }
        });
    }
    
    private void createFloatingNode(String text, String style){
        Label label = new Label(text);
        label.getStyleClass().add(style);
        label.setOpacity(1); 
        
        // Because of the flickering
        // javafx redraws the label everytime so we need to cache it
        label.setCache(true);
        label.setCacheHint(CacheHint.QUALITY); //
        
        // Starting position
        double startX = random.nextDouble() * 400;
        double startY = random.nextDouble() * 300;
        
        // Create random directions for x and o
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
        // Using App.WIDTH/HEIGHT exactly as in your controller
        double width = App.WIDTH;
        double height = App.HEIGHT;
        
        if(width == 0 || height == 0) return;
        
        for(FloatingElement element : floatingElements){
            double newX = element.node.getLayoutX() + element.dx;
            double newY = element.node.getLayoutY() + element.dy;
            
            double nodeWidth = element.node.getBoundsInLocal().getWidth();
            double nodeHeight = element.node.getBoundsInLocal().getHeight();
            
            // Exact collision logic from your StartPageController
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
    
    private static class FloatingElement {
        Node node;
        double dx;
        double dy;

        public FloatingElement(Node node, double dx, double dy) {
            this.node = node;
            this.dx = dx;
            this.dy = dy;
        }
    }
}