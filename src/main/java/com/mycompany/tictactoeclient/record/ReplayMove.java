/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tictactoeclient.record;

/**
 *
 * @author LAPTOP
 */
public class ReplayMove {
    
    private int cellNo;
    private String symbol;

    public ReplayMove(int cellNo, String symbol) {
        this.cellNo = cellNo;
        this.symbol = symbol;
    }

    public int getCellNo() {
        return cellNo;
    }

    public String getSymbol() {
        return symbol;
    }
}
