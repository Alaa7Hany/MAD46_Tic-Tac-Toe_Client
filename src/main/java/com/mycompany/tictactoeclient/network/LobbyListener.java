/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.tictactoeclient.network;

import com.mycompany.tictactoeshared.PlayerDTO;
import java.util.List;

/**
 *
 * @author hp
 */
public interface LobbyListener {
    void onOnlinePlayersUpdated(List<PlayerDTO> players);
}
