
package ChatIT;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ProxyThread extends Thread{
    
    
    
    private ObjectInputStream Clientinput;
    private ObjectOutputStream Clientoutput;
    private ObjectInputStream Serverinput;
    private ObjectOutputStream Serveroutput;
    private Socket connection;
    private Socket server;
    
    public ProxyThread(Socket connection) throws IOException {
        this.connection = connection;
        server = new Socket(InetAddress.getLocalHost(), 6700);
    }
    public void run() {
            try {
                setStreams();
            } catch (IOException ex) {
                System.out.println("IO: setStreams: " + ex);
            }
            boolean chatting = true;
            String textToServer = "";
            String textToClient= "";
            int counter = 0;
            while(chatting) {      
                try {
                    textToServer = (String) Clientinput.readObject();
                    
                } catch (IOException ex) {
                    System.out.println("IO: " + ex);
                } catch (ClassNotFoundException ex) {
                    System.out.println("händer int");
                }
                
                try {
                    Serveroutput.writeObject(textToServer);
                } catch (IOException ex) {
                    Logger.getLogger(ProxyThread.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                try {
                    textToClient = (String) Serverinput.readObject();
                    if(textToClient != "") counter++;
                } catch (IOException ex) {
                    Logger.getLogger(ProxyThread.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(ProxyThread.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                try {
                    try {
                        if(counter%2 == 0) sleep(1000);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(ProxyThread.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    Clientoutput.writeObject(textToClient);
                } catch (IOException ex) {
                    Logger.getLogger(ProxyThread.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                
            }
    }
    
    private void setStreams() throws IOException {
      Clientoutput = new ObjectOutputStream(connection.getOutputStream());
      Clientoutput.flush();
      Clientinput = new ObjectInputStream(connection.getInputStream());
      System.out.println("Client streams setup!");
      
      Serveroutput = new ObjectOutputStream(server.getOutputStream());
      Serveroutput.flush();
      Serverinput = new ObjectInputStream(server.getInputStream());
      System.out.println("Server streams setup!");
}
}
