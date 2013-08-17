
package com.maelr.tcp_phone_door;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerSocketExample {
    private ServerSocket server;
    private int port = 5650;
//    private int SERVERPORT = 8888;
 
    public ServerSocketExample() {
        try {
            server = new ServerSocket(port);
        } catch (IOException e) {
        //	Toast.makeText(This(), "couille serversocketexample" + e,Toast.LENGTH_LONG ).show();
        }
    }
 
    public static void main(String[] args) {
        ServerSocketExample example = new ServerSocketExample();
        example.handleConnection();
    }
 
    public void handleConnection() {
       
 
        //
        // The server do a loop here to accept all connection initiated by the
        // client application.
        //
        while (true) {
            try {
              Socket socket = server.accept();
                new ConnectionHandler(socket);
            } catch (IOException e) {
            //	Toast.makeText(getApplicationContext(), "couille dans handle connection" + e,Toast.LENGTH_LONG ).show();
            }
        }
    }
}
 
class ConnectionHandler implements Runnable {
    private Socket socket;
 
    public ConnectionHandler(Socket socket) {
        this.socket = socket;
 
        Thread t = new Thread(this);
        t.start();
    }
 
    public void run() {
        try
        {
            //
            // Read a message sent by client application
            //
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            String message = (String) ois.readObject();
            System.out.println("Message Received: " + message);
 
            //
            // Send a response information to the client application
            //
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject("Hi...");
 
            ois.close();
            oos.close();
            socket.close();
 
            System.out.println("Waiting for client message...");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
 