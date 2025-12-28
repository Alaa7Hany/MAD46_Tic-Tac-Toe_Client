/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tictactoeclient.record;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author LAPTOP
 */
public class RecordReader {

    public static List<ReplayMove> readMoves(File file) {
        List<ReplayMove> moves = new ArrayList<>();

        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();


                if (!line.startsWith("Move")) continue;

                String[] parts = line.split("\\|");
                if (parts.length < 3) continue;

                String playerPart = parts[1].trim(); 
                String cellPart   = parts[2].trim(); 

                String symbol = playerPart.replace("Player", "").trim();
                int cellNo = Integer.parseInt(
                        cellPart.replace("Cell", "").trim()
                );

                moves.add(new ReplayMove(cellNo, symbol));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return moves;
    }
    
    public static String[] readPlayers(File file) {
        String[] players = new String[2];
        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                if (line.startsWith("Player1:")) {
                    players[0] = line.replace("Player1:", "").trim();
                } else if (line.startsWith("Player2:")) {
                    players[1] = line.replace("Player2:", "").trim();
                }
                if (players[0] != null && players[1] != null) break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return players;
    }
}
