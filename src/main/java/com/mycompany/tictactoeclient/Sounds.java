/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tictactoeclient;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.AudioClip;

/**
 *
 * @author LAPTOP
 */
public class Sounds {
   
    private static MediaPlayer backgroundSound;

    private static AudioClip REGULAR_CLICK;
    private static AudioClip XO_CLICK;
    private static AudioClip TYPE_CLICK;

    public static void playSound() {
        if (SoundManager.isMuted()) return;

        if (backgroundSound == null) {
            Media media = new Media(
                Sounds.class.getResource("/sounds/background2.mp3").toExternalForm()
            );
            backgroundSound = new MediaPlayer(media);
            backgroundSound.setCycleCount(MediaPlayer.INDEFINITE);
            backgroundSound.setVolume(0.4);
        }
        backgroundSound.play();
        
    }

    public static void pauseSound() {
        if (backgroundSound != null) {
            backgroundSound.pause();
        }
    }

    public static void resumeSound() {
        if (backgroundSound != null) {
            backgroundSound.play();
        }
    }


    public static void playXOClick() {
        if (SoundManager.isMuted()) return;

        if (XO_CLICK == null) {
            XO_CLICK = new AudioClip(
                Sounds.class.getResource("/sounds/XO sound.wav").toExternalForm()
            );
        }
        XO_CLICK.play();
    }

    public static void playTypeClick() {
        if (SoundManager.isMuted()) return;

        if (TYPE_CLICK == null) {
            TYPE_CLICK = new AudioClip(
                Sounds.class.getResource("/sounds/Keyboard sound.wav").toExternalForm()
            );
        }
        TYPE_CLICK.play();
    }
    
    public static void playRegularClick() {
        if (SoundManager.isMuted()) return;

        if (REGULAR_CLICK == null) {
            REGULAR_CLICK = new AudioClip(
                Sounds.class.getResource("/sounds/regular click.wav").toExternalForm()
            );
        }
        REGULAR_CLICK.play();
    }
}