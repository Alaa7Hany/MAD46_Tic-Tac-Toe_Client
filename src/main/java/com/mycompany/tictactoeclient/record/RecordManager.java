/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tictactoeclient.record;

import com.mycompany.tictactoeclient.record.model.GameRecord;
import com.mycompany.tictactoeclient.record.model.MoveRecord;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 *
 * @author LAPTOP
 */

public class RecordManager {

    private GameRecord gameRecord;
    private int moveCounter = 0;
    private boolean isRecording = false;

    private String player1;
    private String player2;

    private String result;
    private String winnerName;
    private String loserName;
    private boolean wantSave=false;

    public void startRecord(String gameMode, String difficulty, String p1, String p2) {
        this.player1 = p1;
        this.player2 = p2;

        gameRecord = new GameRecord(
                gameMode,
                difficulty,
                LocalDateTime.now().toString()
        );

        moveCounter = 0;
        isRecording = true;
    }

    public void recordMove(String player, int cellNumber) {
        if (!isRecording || gameRecord == null) return;

        moveCounter++;

        MoveRecord move = new MoveRecord(
                moveCounter,
                player,
                cellNumber,
                LocalDateTime.now().toString()
        );

        gameRecord.addMove(move);
    }
    
    public void enableSave(){
        wantSave=true;
    }

    public void finishRecord(String winner, List<Integer> winningCells) {
        if (!isRecording || gameRecord == null || !wantSave) return;

        gameRecord.setWinner(winner);
        gameRecord.setWinningCells(winningCells);
        gameRecord.setEndTime(LocalDateTime.now().toString());

        if (winner == null || winner.equalsIgnoreCase("DRAW")) {
            result = "Draw";
            winnerName = "None";
            loserName = "None";
        } else if (winner.equalsIgnoreCase(player1)) {
            result = "Win";
            winnerName = player1;
            loserName = player2;
        } else {
            result = "Lose";
            winnerName = player2;
            loserName = player1;
        }

        isRecording = false;
        saveToFile();
    }

    private void saveToFile() {

        File dir = new File("records");
        if (!dir.exists()) dir.mkdirs();

        String baseName = player1 + "vs" + player2;
        int counter = 1;
        File file;

        do {
            file = new File(dir, baseName + "_" + counter + ".txt");
            counter++;
        } while (file.exists());

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {

            writer.write("* Tic Tac Toe Game Record *");
            writer.newLine();

            writer.write("Player1: " + player1);
            writer.newLine();

            writer.write("Player2: " + player2);
            writer.newLine();


            writer.write("Result: " + result);
            writer.newLine();

            writer.write("Game Mode: " + gameRecord.getGameMode());
            writer.newLine();

            writer.write("Difficulty: " + gameRecord.getDifficulty());
            writer.newLine();

            writer.write("Start Time: " + gameRecord.getStartTime());
            writer.newLine();

            writer.write("End Time: " + gameRecord.getEndTime());
            writer.newLine();

            writer.newLine();
            writer.write("Moves:");
            writer.newLine();

            for (MoveRecord move : gameRecord.getMoves()) {
                writer.write(
                        "Move " + move.getMoveNumber() +
                        " | Player " + move.getPlayer() +
                        " | Cell " + move.getCellNumber()
                );
                writer.newLine();
            }
            writer.newLine();
            writer.write("Winner: " + winnerName+", ");
            writer.write("Loser: " + loserName);
            writer.newLine();
            
            writer.newLine();
            writer.write("Winning Cells: " + gameRecord.getWinningCells());
            writer.newLine();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
