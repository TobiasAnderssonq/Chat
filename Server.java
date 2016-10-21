
package ChatIT;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.*;




public class Server {
    private static ServerSocket server;
    private static Socket connection;
    private static Socket newClient;
    //private static ArrayList<chatClient> al;
    public static List<chatClient> chattLista = Collections.synchronizedList(new ArrayList<chatClient>());
    
    public Server() throws IOException, ClassNotFoundException {
        System.out.println("Setting up server on: " + InetAddress.getLocalHost());
        
        server = new ServerSocket(6700, 100, InetAddress.getLocalHost());       
    }  
    
    
    private static Socket waitForConnection() throws IOException{
        connection = server.accept();
        System.out.println("Connected to " + connection.getInetAddress());
        return connection;
    }
    
    private static class listHandler extends Thread{
            
            public void run() {
                while(true) {
                    setList();
                }
            }
            
            private static void setList() {
        
        for (int i = 0; i < chattLista.size(); i++) {
            
            if(!chattLista.get(i).isAlive()) chattLista.remove(i);        
        }
         }
   }
        
        
  
    public List getChatList() {
        return chattLista;
    }        
    
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        
        Server server = new Server();
        listHandler listhandler = new listHandler();
        listhandler.start();
        
        
       
         while(true) {
           
             newClient = waitForConnection();
             chatClient c = new chatClient(newClient);    
             c.start();
             chattLista.add(c);
             
             
        }
        
    }
    
}
