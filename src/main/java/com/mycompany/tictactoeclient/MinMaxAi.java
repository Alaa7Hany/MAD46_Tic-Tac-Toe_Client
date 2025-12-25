/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tictactoeclient;

import java.util.List;
import java.util.Random;

/**
 *
 * @author emada
 */
public class MinMaxAi {

    public static int getBestMove(List<Integer> xSteps, List<Integer> oSteps, boolean hard) {
        List<Integer> available = GameHelper.getAvailableCells(xSteps, oSteps);

        if (!hard && new Random().nextBoolean()) {
            return available.get(new Random().nextInt(available.size()));
        }

        int bestScore = Integer.MIN_VALUE;
        int bestMove = available.get(0);

        for (int cell : available) {
            oSteps.add(cell);
            int score = minimax(xSteps, oSteps, false);
            oSteps.remove((Integer) cell);

            if (score > bestScore) {
                bestScore = score;
                bestMove = cell;
            }
        }
        return bestMove;
    }

    private static int minimax(List<Integer> xSteps, List<Integer> oSteps, boolean isMax) {
        if (GameHelper.checkWin(oSteps)) return 10;
        if (GameHelper.checkWin(xSteps)) return -10;
        if (GameHelper.checkDraw(xSteps, oSteps)) return 0;

        List<Integer> available = GameHelper.getAvailableCells(xSteps, oSteps);

        if (isMax) {
            int best = Integer.MIN_VALUE;
            for (int cell : available) {
                oSteps.add(cell);
                best = Math.max(best, minimax(xSteps, oSteps, false));
                oSteps.remove((Integer) cell);
            }
            return best;
        } else {
            int best = Integer.MAX_VALUE;
            for (int cell : available) {
                xSteps.add(cell);
                best = Math.min(best, minimax(xSteps, oSteps, true));
                xSteps.remove((Integer) cell);
            }
            return best;
        }
    }
}