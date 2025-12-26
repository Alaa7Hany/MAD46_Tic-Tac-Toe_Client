/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.tictactoeclient;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;

/**
 * FXML Controller class
 *
 * @author LAPTOP
 */
public class AboutDialogController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void closeDialog(ActionEvent event) {
        Node source = (Node) event.getSource();
        StackPane root = (StackPane) source.getScene().getRoot();

        int size = root.getChildren().size();
        if (size >= 2) {
            root.getChildren().remove(size - 1);
            root.getChildren().remove(size - 2);
        }
    }
    
}
