/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.tictactoeclient;

import com.mycompany.tictactoeclient.network.NetworkConnection; // Import this
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;

public class ServerConfigureDialogController implements Initializable {

    @FXML
    private TextField ipFiled; 

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ipFiled.setPromptText("Ex: 192.168.1.5");
    }    

    private void onCancel(ActionEvent event) {
        Node source = (Node) event.getSource();
        StackPane parent = (StackPane) source.getScene().getRoot();
        
        parent.getChildren().remove(parent.getChildren().size() - 1);
    }

    @FXML
    private void onSubmit(ActionEvent event) {
        String ip = ipFiled.getText();
        if (ip != null && !ip.trim().isEmpty()) {
            connectAndNavigate(ip.trim());
        } else {
             System.out.println("IP is empty");
        }
    }

    @FXML
    private void onLocalHost(ActionEvent event) {
        connectAndNavigate("localhost");
    }
    
    private void connectAndNavigate(String ip) {
        NetworkConnection.setServerIp(ip);
        
        try {
            App.setRoot(Pages.loginPage);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
