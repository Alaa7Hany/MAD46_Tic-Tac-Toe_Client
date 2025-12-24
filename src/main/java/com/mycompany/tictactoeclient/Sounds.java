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
    
    private static final AudioClip REGULAR_CLICK =
        new AudioClip(Sounds.class
            .getResource("/sounds/regular click.wav").toExternalForm());
    
    private static final AudioClip XO_CLICK =
        new AudioClip(Sounds.class
            .getResource("/sounds/XO sound.wav").toExternalForm());
    
    private static final AudioClip TYPE_CLICK =
        new AudioClip(Sounds.class
            .getResource("/sounds/Keyboard sound.wav").toExternalForm());
    
    
    public static void playSound(){
        Media media =  new Media(Sounds.class.getResource("/sounds/background.mp3").toExternalForm());
        backgroundSound = new MediaPlayer(media);
        
        backgroundSound.setCycleCount(MediaPlayer.INDEFINITE);
        backgroundSound.setVolume(0.4);
        backgroundSound.play();
    }
    
    public static void pauseSound(){
        if(backgroundSound != null){
            backgroundSound.pause();
       }
    }
    
    public static void resumeSound(){
        if(backgroundSound != null){
            backgroundSound.play();
        }
    }
    
    public static void playUiClick() {
        REGULAR_CLICK.play();
    }

    public static void playXOClick() {
        XO_CLICK.play();
    }

    public static void playTypeClick() {
        TYPE_CLICK.play();
    }
    
}
