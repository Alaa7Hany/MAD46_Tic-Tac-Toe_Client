package com.mycompany.tictactoeclient;

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
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;
    private final static String rootPage = Pages.loginPage;
    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML(rootPage), 615, 577);

        stage.setScene(scene);
        stage.setResizable(false);
        scene.setFill(Color.TRANSPARENT);
        Sounds.playSound();
        
        scene.addEventFilter(javafx.scene.input.MouseEvent.MOUSE_PRESSED, event -> {
        if (!event.isPrimaryButtonDown()) return;   //  left mouse button only
        javafx.scene.Node node = (javafx.scene.Node) event.getTarget();
        if (node.getStyleClass().contains("xo-cell")) return;
        Sounds.playUiClick();
        });
 
        stage.show();
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

