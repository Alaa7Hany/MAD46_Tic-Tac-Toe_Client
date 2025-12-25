/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tictactoeclient;

/**
 *
 * @author LAPTOP
 */
public class SoundManager {
    
    private static boolean muted = false;

    public static boolean isMuted() {
        return muted;
    }

    public static void toggleMute() {
        muted = !muted;

        if (muted) {
            Sounds.pauseSound();
        } else {
            Sounds.resumeSound();
        }
    }

    public static void applyState() {
        if (muted) {
            Sounds.pauseSound();
        } else {
            Sounds.resumeSound();
        }
    }
    
}
