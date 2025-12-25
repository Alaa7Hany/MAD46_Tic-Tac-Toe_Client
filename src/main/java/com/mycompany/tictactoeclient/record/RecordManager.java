/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tictactoeclient.record;

import com.mycompany.tictactoeclient.record.model.GameRecord;
import com.mycompany.tictactoeclient.record.model.MoveRecord;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

/**
 *
 * @author LAPTOP
 */

public class RecordManager {

    private GameRecord gameRecord;
    private int moveCounter = 0;
    private boolean isRecording = false;

    public void startRecord(String gameMode, String difficulty) {
        gameRecord = new GameRecord(
                gameMode,
                difficulty,
                LocalDateTime.now().toString()
        );
        isRecording = true;
        moveCounter = 0;
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
        System.out.println(
        "Recorded move #" + moveCounter +
        " Player=" + player +
        " Cell=" + cellNumber
    );
    }

    public void finishRecord(String winner, java.util.List<Integer> winningCells) {
        if (!isRecording || gameRecord == null) return;

        gameRecord.setWinner(winner);
        gameRecord.setWinningCells(winningCells);
        gameRecord.setEndTime(LocalDateTime.now().toString());
        isRecording = false;
        
        saveToFile();
    }

    public GameRecord getGameRecord() {
        return gameRecord;
    }
    
    public void debugPrintRecord() {
    if (gameRecord == null) {
        System.out.println("No record");
        return;
    }

    System.out.println("=== GAME RECORD DEBUG ===");
    System.out.println("Mode: " + gameRecord.getGameMode());
    System.out.println("Difficulty: " + gameRecord.getDifficulty());

    for (MoveRecord m : gameRecord.getMoves()) {
        System.out.println(
            "Move " + m.getMoveNumber() +
            " | Player " + m.getPlayer() +
            " | Cell " + m.getCellNumber() +
            " | Time " + m.getTime()
        );
    }
    }
    
    public void saveToFile() {
        if (gameRecord == null) {
            System.out.println("No record to save");
            return;
        }

        String fileName = "record_" + System.currentTimeMillis() + ".txt";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {

            writer.write("=== Tic Tac Toe Game Record ===");
            writer.newLine();

            writer.write("Start Time: " + gameRecord.getStartTime());
            writer.newLine();

            writer.write("End Time: " + gameRecord.getEndTime());
            writer.newLine();

            writer.write("Game Mode: " + gameRecord.getGameMode());
            writer.newLine();

            writer.write("Difficulty: " + gameRecord.getDifficulty());
            writer.newLine();

            writer.newLine();
            writer.write("Moves:");
            writer.newLine();

            for (MoveRecord move : gameRecord.getMoves()) {
                writer.write(
                    "Move " + move.getMoveNumber() +
                    " | Player " + move.getPlayer().toUpperCase() +
                    " | Cell " + move.getCellNumber() +
                    " | Time " + move.getTime()
                );
                writer.newLine();
            }

            writer.newLine();
            writer.write("Winner: " + gameRecord.getWinner());
            writer.newLine();

            writer.write("Winning Cells: " + gameRecord.getWinningCells());
            writer.newLine();

            writer.write("================================");

            System.out.println("Record saved to file: " + fileName);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
