
package ChatIT;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;



public class chatClient extends Thread{
    
        private ObjectInputStream input;
        private ObjectOutputStream output;
        private Socket connection;
        boolean chatting = true;          
        private String text;
        
        public chatClient(Socket sock) throws IOException {
            text = "";
            this.connection = sock;
            
        }
        
        public void run(){
            try {
                setStreams();
            } catch (IOException ex) {
                System.out.println("IO: setStreams: " + ex);
            }
                   
            while(chatting) {      
                try {
                    text = (String) input.readObject();
                    System.out.println(text);
                } catch (IOException ex) {
                    System.out.println("IO: " + ex);
                } catch (ClassNotFoundException ex) {
                    System.out.println("händer int");
                }
                if(text.toLowerCase().contains(".howmany?")) {NumberOfParticipants();}
                broadcast(text);
                chatting = !text.contains(".done");
               
                
            }
        
        System.out.println("Client " + connection.getInetAddress() +  " has disconnected!");
        
            try {
                closeConnection();
            } catch (IOException ex) {
                System.out.println("IO closeConnections: " + ex);
            }
           
        }
        
        public String getText(){
            return this.text;
        }
        
        private void setStreams() throws IOException {
            output = new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            input = new ObjectInputStream(connection.getInputStream());
           System.out.println("Server streams setup!");
        }
        
        
        private void closeConnection() throws IOException {
            if(input != null) input.close();
            if(output != null)output.close();
            if(connection != null) connection.close();
        }
        
        private void NumberOfParticipants(){
            try {          
                output.writeObject(Integer.toString(Server.chattLista.size()) + " clients are currently connected!");
            } catch (IOException ex) {
                System.out.println("IO NumberOfParticipants: " + ex);
            }
        }
        
        public void receive(String text) {
            try {
                output.writeObject(text);
                output.flush();
            } catch (IOException ex) {
                System.out.println("IO broadcast: "+ex);
            }
            
            
        }
        
        private synchronized void broadcast(String text) {
                   
            for (int i = 0; i < Server.chattLista.size(); i++) {
                Server.chattLista.get(i).receive(text);
            }
          return;
            
    }
}
