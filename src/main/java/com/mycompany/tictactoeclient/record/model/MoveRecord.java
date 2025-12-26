/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tictactoeclient.record.model;

/**
 *
 * @author LAPTOP
 */

public class MoveRecord {

    private int moveNumber;
    private String player;  
    private int cellNumber;
    private String time;

    public MoveRecord(int moveNumber, String player, int cellNumber, String time) {
        this.moveNumber = moveNumber;
        this.player = player;
        this.cellNumber = cellNumber;
        this.time = time;
    }

    public int getMoveNumber() {
        return moveNumber;
    }

    public String getPlayer() {
        return player;
    }

    public int getCellNumber() {
        return cellNumber;
    }

    public String getTime() {
        return time;
    }
}
