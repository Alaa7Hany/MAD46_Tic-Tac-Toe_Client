/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tictactoeclient.network;

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
    
    private NetworkConnection(){
        try {
            socket = new Socket(InetAddress.getLocalHost(), 5005);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
        } catch (UnknownHostException ex) {
            System.getLogger(NetworkConnection.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        } catch (IOException ex) {
            System.getLogger(NetworkConnection.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
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
    
    public void closeConnection() throws IOException{
        if (in != null) in.close();
        if (out != null) out.close();
        if (socket != null && !socket.isClosed()) {
            socket.close();
        }
    }
    
}
