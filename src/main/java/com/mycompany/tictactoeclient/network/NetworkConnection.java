/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tictactoeclient.network;

import com.mycompany.tictactoeclient.App;
import com.mycompany.tictactoeclient.LobbyPageController;
import com.mycompany.tictactoeclient.Pages;
import com.mycompany.tictactoeshared.InvitationDTO;
import com.mycompany.tictactoeshared.Request;
import com.mycompany.tictactoeshared.Response;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import javafx.application.Platform;

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
    
    public Response sendRequest(Request request) {
        // Check if the output stream is initialized
        // in case in initial connection when the server is down
        if (out == null) {
            connection = null; 
            return new Response(Response.Status.FAILURE, "Server Not Available");
        }

        try {
            out.writeObject(request);
            out.flush();
            Response response = (Response) in.readObject();
            return response;
        } catch (IOException | ClassNotFoundException ex) {
            //  because when the server shuts we want to reset the connection to a new connection
            try {
                closeConnection();
            } catch (IOException e) {
                e.printStackTrace();
            }
            // reset the connection
            connection = null; 
            return new Response(Response.Status.FAILURE, "Connection Error");
        }
    }
    
   private void listenLoop() {
        try {
            while (true) {
                Object obj = in.readObject();   
                if (!(obj instanceof Request)) continue;

                Request request = (Request) obj;

                switch (request.getType()) {

                    case INVITE_RECEIVED : {
                        InvitationDTO dto =
                            (InvitationDTO) request.getData();

                        if (invitationListener != null) {
                            Platform.runLater(() ->
                                invitationListener.onInvitationReceived(dto)
                            );
                        }
                         break;
                    }

                    case INVITE_REJECTED : {
                        Platform.runLater(() ->
                            LobbyPageController.instance.onInviteRejected()
                        );
                         break;
                    }

                    case START_GAME : {
                        Platform.runLater(() -> {
                            try {
                                App.navigateTo(Pages.gamePage);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        });
                         break;
                    }

                    default : {
                        System.out.println("Unhandled push: " + request.getType());
                         break;
                    }
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Listener stopped");
        }
    }

    
        public ObjectInputStream getInputStream() {
        return in;
    }
       public void sendMessage(Request request) {
        if (out == null) return;
        try {
            out.writeObject(request);
            out.flush();
        } catch (IOException ex) {
            ex.printStackTrace();
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
