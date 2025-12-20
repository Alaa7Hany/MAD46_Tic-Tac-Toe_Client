/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tictactoeclient.network;

import com.mycompany.tictactoeshared.InvitationDTO;
import com.mycompany.tictactoeshared.LoginDTO;
import com.mycompany.tictactoeshared.Request;
import com.mycompany.tictactoeshared.RequestType;
import com.mycompany.tictactoeshared.Response;

/**
 *
 * @author hp
 */
public class NetworkDAO {
    
    private static NetworkDAO instance;
    
    private NetworkDAO(){
        
    }
    
    public static NetworkDAO getInstance(){
        if(instance == null)
            instance = new NetworkDAO();
        return instance;
    }
    
    public Response login(String username, String password){
        LoginDTO loginData = new LoginDTO(username, password);
        
        Request request = new Request(RequestType.LOGIN, loginData);
        
        Response response = NetworkConnection.getConnection().sendRequest(request);
        
        return response;
    }
    
    public Response sendInvite(String fromUser, String toUser) {
        
        InvitationDTO inviteDTo = new InvitationDTO(fromUser, toUser);
        
        Request request = new Request(RequestType.INVITE_PLAYER, inviteDTo);
        
        Response response= NetworkConnection.getConnection().sendRequest(request);
        
        return response;
    }

    
}
