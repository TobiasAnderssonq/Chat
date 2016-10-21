
package ChatIT;

import static ChatIT.Server.chattLista;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;


public class DelayProxy {
    private static ServerSocket proxyServer;
    private static Socket Clientconnection;
    private static Socket newClient;
    
    public DelayProxy() throws IOException, ClassNotFoundException {
        System.out.println("Setting up server on: " + InetAddress.getLocalHost());
        
        proxyServer = new ServerSocket(6800, 100, InetAddress.getLocalHost());          
    }  
    
    private static Socket waitForConnection() throws IOException{
        Clientconnection = proxyServer.accept();
        System.out.println("Connected to " + Clientconnection.getInetAddress());
        return Clientconnection;
    }
    
    
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        DelayProxy prox = new DelayProxy();
        
        while(true) {
             newClient = waitForConnection();
             ProxyThread c = new ProxyThread(newClient);    
             c.start();
             
        }
    }
    
}
