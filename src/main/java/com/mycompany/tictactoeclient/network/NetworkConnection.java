/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tictactoeclient.network;

import com.mycompany.tictactoeshared.InvitationDTO;
import com.mycompany.tictactoeshared.Request;
import com.mycompany.tictactoeshared.Response;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 *
 * @author hp
 */
public class NetworkConnection {
    
    private static NetworkConnection connection;
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private InvitationListener invitationListener;
    
    private NetworkConnection(){
        try {
            socket = new Socket(InetAddress.getLocalHost(), 5005);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
            
            new Thread(this::listenLoop).start(); 
              
        } catch (UnknownHostException ex) {
            System.getLogger(NetworkConnection.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        } catch (IOException ex) {
            System.getLogger(NetworkConnection.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }
    
    public void setInvitationListener(InvitationListener listener) {
        this.invitationListener = listener;
    }

    // Singleton, only one instance of the class
    public static NetworkConnection getConnection(){
        if(connection == null)
            connection = new NetworkConnection();
        return connection;
    }
    
    public Response sendRequest(Request request){
        try {
            out.writeObject(request);
            out.flush();
            Response response = (Response) in.readObject();
            return response;
        } catch (IOException | ClassNotFoundException ex) {
            return new Response(Response.Status.FAILURE, "Conection Erro");
        }
    }
    
    private void listenLoop() {
        try {
            while (true) {
                Object obj = in.readObject();   
                if (!(obj instanceof Response)) continue;

                Response response = (Response) obj;

                // Check if this is an invite (data is InvitationDTO)
                if (response.getData() instanceof InvitationDTO) {
                    InvitationDTO dto = (InvitationDTO) response.getData();
                    if (invitationListener != null) {
                        invitationListener.onInvitationReceived(dto);
                    } else {
                        System.out.println("Invitation from " + dto.getFromUsername());
                    }
                } else {
                    System.out.println("Received async response: " + response.getData());
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Listener stopped: " + e.getMessage());
        }
    }
    
    public void closeConnection() throws IOException{
        if (in != null) in.close();
        if (out != null) out.close();
        if (socket != null && !socket.isClosed()) {
            socket.close();
        }
    }
    
}
