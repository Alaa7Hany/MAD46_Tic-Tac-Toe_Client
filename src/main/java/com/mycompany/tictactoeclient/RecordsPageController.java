/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.tictactoeclient;


import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Scanner;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
/**
 * FXML Controller class
 *
 * @author emada
 */
public class RecordsPageController implements Initializable {

    @FXML
    private Button backButton;

    @FXML
    private VBox recordsContainer;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loadRecords();
    }

    private ListView<?> recordsListView; 
    /**
     * Initializes the controller class.
     */
    

private void loadRecordsGame(){

}    
    
    @FXML
    private void moveBack(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/mycompany/tictactoeclient/startPage.fxml")
            );

            Pane newContent = loader.load();
            Pane currentRoot = (Pane) backButton.getScene().getRoot();

            currentRoot.getChildren().setAll(newContent);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadRecords() {
        File dir = new File("records");
        if (!dir.exists()) return;

        File[] files = dir.listFiles((d, name) -> name.endsWith(".txt"));
        if (files == null) return;

        recordsContainer.getChildren().clear();
        
        int index = 1;
        for (File file : files) {
            recordsContainer.getChildren().add(createRecordCard(file,index));
            index++;
        }
    }

    private HBox createRecordCard(File file, int index) {

        String player1 = "";
        String player2 = "";
        String winner = "";
        String loser = "";
        String result = "";

        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();

                if (line.startsWith("Player1:"))
                    player1 = line.replace("Player1:", "").trim();
                else if (line.startsWith("Player2:"))
                    player2 = line.replace("Player2:", "").trim();
                else if (line.startsWith("Winner:"))
                    winner = line.replace("Winner:", "").trim();
                else if (line.startsWith("Loser:"))
                    loser = line.replace("Loser:", "").trim();
                else if (line.startsWith("Result:"))
                    result = line.replace("Result:", "").trim();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        String displayText= "#" + index + " " + player1+ " VS " + player2;

        Label title = new Label(displayText);
        title.getStyleClass().add("record-title");

        ImageView icon = new ImageView();
        icon.setFitWidth(36);
        icon.setFitHeight(36);
        icon.setPreserveRatio(true);

        if (result.equalsIgnoreCase("Win")) {
            icon.setImage(new Image(getClass().getResourceAsStream("/images/won.png")));
        } else if (result.equalsIgnoreCase("Lose")) {
            icon.setImage(new Image(getClass().getResourceAsStream("/images/lost.png")));
        } else {
            icon.setImage(new Image(getClass().getResourceAsStream("/images/draw.png")));
        }

        Pane iconPane = new Pane(icon);
        iconPane.setPrefWidth(50);

        Button viewBtn = new Button("View");
        viewBtn.getStyleClass().add("record-view-btn");
        viewBtn.setOnAction(e -> openRecord(file));

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox card = new HBox(iconPane, title, spacer, viewBtn);
        card.setPrefWidth(360);
        card.setSpacing(15);
        card.setPadding(new Insets(15));
        card.setAlignment(Pos.CENTER_LEFT);

        card.getStyleClass().add("record-card");

        return card;
    }

    private void openRecord(File file) {

    StringBuilder content = new StringBuilder();

    try (Scanner scanner = new Scanner(file)) {
        while (scanner.hasNextLine()) {
            content.append(scanner.nextLine()).append("\n");
        }
    } catch (Exception e) {
        e.printStackTrace();
        return;
    }

    
    TextArea textArea = new TextArea(content.toString());
    textArea.setEditable(false);
    textArea.setWrapText(true);
    textArea.setPrefSize(500, 400);
    textArea.getStyleClass().add("textarea-view");

    Dialog<Void> dialog = new Dialog<>();
    dialog.setTitle("TIC TAC TOE");
    dialog.getDialogPane().setContent(textArea);
    dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);

    dialog.showAndWait();
}
}
