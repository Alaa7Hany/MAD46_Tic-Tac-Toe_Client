/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tictactoeclient.record.model;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author LAPTOP
 */

public class GameRecord {

    private String gameMode;
    private String difficulty;
    private String startTime;
    private String endTime;

    private List<MoveRecord> moves = new ArrayList<>();

    private String winner;
    private List<Integer> winningCells = new ArrayList<>();

    public GameRecord(String gameMode, String difficulty, String startTime) {
        this.gameMode = gameMode;
        this.difficulty = difficulty;
        this.startTime = startTime;
    }

    public void addMove(MoveRecord move) {
        moves.add(move);
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }

    public void setWinningCells(List<Integer> winningCells) {
        this.winningCells = winningCells;
    }

    public String getGameMode() {
        return gameMode;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public List<MoveRecord> getMoves() {
        return moves;
    }

    public String getWinner() {
        return winner;
    }

    public List<Integer> getWinningCells() {
        return winningCells;
    }
}