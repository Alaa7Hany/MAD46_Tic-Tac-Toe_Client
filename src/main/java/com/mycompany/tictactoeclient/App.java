package com.mycompany.tictactoeclient;


import com.mycompany.tictactoeshared.PlayerDTO;
import com.mycompany.tictactoeclient.network.NetworkConnection;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.function.Consumer;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.input.MouseEvent;
import javafx.scene.Node;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.util.Duration;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;
    private final static String rootPage = Pages.lobbyPage;
    private MouseEvent mouseEvent;
    private Node node;

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML(rootPage), 615, 577);

        stage.setScene(scene);
        stage.setResizable(false);
        scene.setFill(Color.TRANSPARENT);
        Sounds.playSound();
        
        scene.addEventFilter(mouseEvent.MOUSE_PRESSED, event -> {
        if (!event.isPrimaryButtonDown()) return;   //  left mouse button only
        node = (Node) event.getTarget();
        if (node.getStyleClass().contains("xo-cell")) return;
        Sounds.playUiClick();
        });
 
        stage.show();
    }
    
    static void addProgressIndicator(StackPane sp){
        Region dimmer = new Region();
        dimmer.setStyle("-fx-background-color: rgba(0, 0, 0, 0.5);");

        dimmer.prefWidthProperty().bind(sp.widthProperty());
        dimmer.prefHeightProperty().bind(sp.heightProperty());

        ProgressIndicator pi = new ProgressIndicator();

        Platform.runLater(()->{
            sp.getChildren().addAll(dimmer, pi);
        });
    }
    
    static void removeProgressIndicator(StackPane sp){
        Platform.runLater(()->{
            int size = sp.getChildren().size();
            
            if (size > 0) {
                sp.getChildren().remove(size - 1); 
            }
            
            size = sp.getChildren().size();
            if (size > 0) {
                sp.getChildren().remove(size - 1);
            }
        });
    }
    
    static void showAlertMessage(StackPane sp, String message, boolean isSuccess) {
        // 1. Create the label
        Label toast = new Label(message);

        // 2. Simple Styling (Green for success, Red for failure)
        String bgColor = isSuccess ? "rgba(0, 128, 0, 0.7)" : "rgba(255, 0, 0, 0.7)";
        toast.setStyle("-fx-background-color: " + bgColor + ";" +
                       "-fx-text-fill: white;" +
                       "-fx-font-weight: bold;" +
                       "-fx-padding: 10px 20px;" +
                       "-fx-background-radius: 20;");

        // 3. Position it (Bottom Center looks best for toasts)
        // Note: You can remove this to center it by default
        StackPane.setAlignment(toast, Pos.BOTTOM_CENTER);
        StackPane.setMargin(toast, new javafx.geometry.Insets(0, 0, 50, 0)); // 50px from bottom

        // 4. Add to the provided StackPane
        sp.getChildren().add(toast);

        // 5. Animation: Wait 1.5s, then Fade Out, then Remove
        FadeTransition fade = new FadeTransition(Duration.seconds(1.5), toast);
        fade.setFromValue(1.0); // Fully visible
        fade.setToValue(0.0);   // Transparent
        fade.setDelay(Duration.seconds(1.5)); // Wait before fading
        fade.setOnFinished(e -> sp.getChildren().remove(toast)); // Cleanup
        fade.play();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }
    
    // We will use this method to pass parameters between pages using lambda 
    static <T> void setRoot(String fxml, Consumer<T> controllerAction) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        Parent root = fxmlLoader.load();

        T controller = fxmlLoader.getController();

        controllerAction.accept(controller);
        
        scene.setRoot(root);
    }
    
    
    // To show an fxml dialog with a dim background
    public static void showMyFxmlDialog(StackPane rootStackPane, String dialogPath, boolean isCloseable) throws IOException {
        
        Parent dialog = loadFXML(dialogPath);
        
        Region dimmer = new Region();
        dimmer.setStyle("-fx-background-color: rgba(0, 0, 0, 0.5);");
        
        dimmer.prefWidthProperty().bind(rootStackPane.widthProperty());
        dimmer.prefHeightProperty().bind(rootStackPane.heightProperty());

        rootStackPane.getChildren().addAll(dimmer, dialog);
        if(isCloseable){ 
            dimmer.setOnMouseClicked(event -> {
                rootStackPane.getChildren().removeAll(dimmer, dialog);
            });
        }
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    @Override
    public void stop() throws Exception {
        NetworkConnection.getConnection().closeConnection();
        super.stop(); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody
    }
    

    public static void main(String[] args) {
     
        launch();
  
    }
}

