/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tictactoeclient.network;

import com.mycompany.tictactoeclient.App;
import com.mycompany.tictactoeclient.GamePageController;
import com.mycompany.tictactoeclient.LobbyPageController;
import com.mycompany.tictactoeclient.Pages;
import com.mycompany.tictactoeclient.enums.Difficulty;
import com.mycompany.tictactoeclient.enums.GameMode;
import com.mycompany.tictactoeshared.InvitationDTO;
import com.mycompany.tictactoeshared.PlayerDTO;
import com.mycompany.tictactoeshared.Request;
import static com.mycompany.tictactoeshared.RequestType.INVITE_REJECTED;
import com.mycompany.tictactoeshared.Response;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;
import javafx.application.Platform;
import javafx.scene.control.Alert;

/**
 *
 * @author hp
 */
public class NetworkConnection {
    private   static boolean flag = true;
    private static NetworkConnection connection;
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private InvitationListener invitationListener;
    private LobbyListener lobbyListener;
    
    
    public static void reConnectListener(){
        flag = true;
    }
    public static void disconnectListener(){
        flag = false;
    }
    private NetworkConnection(){
        try {
            System.out.println("Creating NetworkConnection...");
            socket = new Socket(InetAddress.getLocalHost(), 5005);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
            System.out.println("Socket created: " + socket.getLocalPort());
            
              
        } catch (UnknownHostException ex) {
            System.getLogger(NetworkConnection.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        } catch (IOException ex) {
            System.getLogger(NetworkConnection.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }
    
    public void setInvitationListener(InvitationListener listener) {
        this.invitationListener = listener;
    }
    public void setLobbyListener(LobbyListener listener) {
        this.lobbyListener = listener;
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

        System.out.println("We r in NC above try ... send request ");
        try {
            out.writeObject(request);
            out.flush();
            Response response =(Response) in.readObject();
            return response;

        } catch (IOException | ClassNotFoundException ex) {
            ex.printStackTrace();
            try { closeConnection(); } catch (IOException e) {}
            connection = null;
            return new Response(Response.Status.FAILURE, "Connection Error");
        }
    }
    
    public void sendInvitation(Request request) {
        // Check if the output stream is initialized
        // in case in initial connection when the server is down
        if (out == null) {
            connection = null; 
           // return new Response(Response.Status.FAILURE, "Server Not Available");
        }

        System.out.println("We r in NC above try ... send request ");
        try {
            out.writeObject(request);
            out.flush();
            

        } catch (IOException ex) {
            ex.printStackTrace();
            try { closeConnection(); } catch (IOException e) {}
            connection = null;
            //return new Response(Response.Status.FAILURE, "Connection Error");
        }
    }
    
   /* private void handleRequest(Request push) {
        switch (push.getType()) {
            case INVITE_RECEIVED:
                InvitationDTO invite = (InvitationDTO) push.getData();
                Platform.runLater(() -> {
                    if (invitationListener != null) {
                        invitationListener.onInvitationReceived(invite);
                    }
                });
                break;

            case INVITE_REJECTED:
                Platform.runLater(() -> {
                    if (LobbyPageController.instance != null) {
                        LobbyPageController.instance.onInviteRejected();
                    }
                });
                break;

            case START_GAME:
                StartGameDTO startData = (StartGameDTO) push.getData();
                Platform.runLater(() -> {
                    try {
                        App.navigateTo(Pages.gamePage); // pass client handler 
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                break;

            default:
                System.out.println("Unhandled push: " + push.getType());
        }
    }*/
    
   
   private void listenLoop() {
  
        try {
            while (flag) {
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

                    case ACCEPT_INVITE : {
                        
                        flag=false;
                       InvitationDTO twoPlayer = (InvitationDTO) request.getData();
                        Platform.runLater(() -> {
                            try {

                                App.setRoot(Pages.gamePage, (GamePageController gameController) -> {
                                    gameController.initGame(twoPlayer,GameMode.ONLINE,Difficulty.EASY ,twoPlayer.getFromUsername().getScore() ,twoPlayer.getToUsername().getScore() );
                                });    
                                
                            } catch (IOException ex) {
                                System.getLogger(NetworkConnection.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
                            }
                        });
                         break;
                    }
                    case GET_ONLINE_PLAYERS : {
                        List<PlayerDTO> players = (List<PlayerDTO>) request.getData();
                        if (lobbyListener != null) {
                            Platform.runLater(() -> 
                                lobbyListener.onOnlinePlayersUpdated(players)
                            );
                        }
                        break;
                    }

                    default : {
                        System.out.println("Unhandled push: " + request.getType());
                         break;
                    }
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            
            //////////////// Handling Server Disconnection, Don't touch ///////////////////
            
            System.out.println("Connection Lost"+e.getMessage());
            
            try {
                closeConnection();
            } catch (IOException ex) {
                System.getLogger(NetworkConnection.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            }
            
            if(flag){
                Platform.runLater(()->{
                    flag=false;
                    
                    try {
                        App.setRoot(Pages.startPage);
                    } catch (IOException ex) {
                        System.getLogger(NetworkConnection.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
                    }

                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Disconnected from server.");
                    alert.show();
                });
            }
            
            ///////////////////////////////////////////////////////////////////
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
    
    public void startLobbyListener(){
        new Thread(this::listenLoop).start();
    }
    
}
