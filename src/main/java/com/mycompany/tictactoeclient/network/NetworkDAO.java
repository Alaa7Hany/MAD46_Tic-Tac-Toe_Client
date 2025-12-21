/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tictactoeclient.network;

import com.mycompany.tictactoeshared.LoginDTO;
import com.mycompany.tictactoeshared.MoveDTO;
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
    public Response register(String username, String password){
        LoginDTO loginData = new LoginDTO(username, password);
        
        Request request = new Request(RequestType.REGISTER, loginData);
        
        Response response = NetworkConnection.getConnection().sendRequest(request);
        
        return response;
    }


    
    public Response lobby(){
        Request request = new Request(RequestType.GET_ONLINE_PLAYERS, null);
        
        Response response = NetworkConnection.getConnection().sendRequest(request);
        return response;
    }
    
      public void sendMove (String sessionId,int cellNo,String symbol,boolean win ,boolean draw){
        MoveDTO moveData = new MoveDTO(sessionId,cellNo, symbol,win,draw);
        Request request = new Request(RequestType.MOVE, moveData);
        NetworkConnection.getConnection().sendMessage(request);
    }
    

}
