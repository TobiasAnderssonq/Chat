
package ChatIT;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import static java.lang.Thread.sleep;
import java.net.*;
import java.util.Scanner;
import javax.swing.*;
import javax.swing.text.DefaultCaret;
import static javax.swing.text.DefaultCaret.ALWAYS_UPDATE;

public class Client extends JFrame implements ActionListener{
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private InetAddress serverIP;
    private static ChatWindow c;
    
    Scanner sc = new Scanner(System.in);
    
    public Client(InetAddress host) throws InterruptedException  {
        
            serverIP = host;
            try {
                socket = new Socket(host, 6700);
                setStreams();
            } catch (IOException ex) {
                
            }
            String alias = "";
            String textLine = "";
            System.out.println("To exit, write '.done'.");
            System.out.println("Choose an alias: ");
            alias = sc.nextLine();
            messageHandler handler = new messageHandler(in, c.getMsgarea());
            handler.start();
            
            while(!textLine.equals(".done") && c.isVisible()) {
                
                
                textLine = c.msg;
                
                sleep(10);
                if(!textLine.equals("")) {
                    try {
                        
                        out.writeObject(alias + "> " + textLine);
                        out.flush();
                        c.setMsgWrite();
                        c.setMsg();
                    } catch (IOException ex) {
                        System.out.println("IO: " + ex);
                    }   
                }
                
            }
            
            
                handler.stop();
                
                try {
                closeConnection();
                } catch (IOException ex) {
                System.out.println("IO closeConnection: " + ex);
                }
                c.dispose();
              
           
        }
    
    private void setStreams() throws IOException {
          out = new ObjectOutputStream(socket.getOutputStream());
          out.flush();
          in = new ObjectInputStream(socket.getInputStream());
          System.out.println("Client streams setup!");
    }
    private void closeConnection() throws IOException {
            if(in != null) in.close();
            if(out != null)out.close();
            if(socket != null) socket.close();
    }


    public static void main(String[] args) throws IOException, InterruptedException {
        
        c = new ChatWindow();
        c.setVisible(true);
        c.setResizable(false);
        c.setLocation(500, 200);
        c.getRootPane().setDefaultButton(c.messageSend);
        c.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        
        DefaultCaret caret = (DefaultCaret) c.getMsgarea().getCaret();
        caret.setUpdatePolicy(ALWAYS_UPDATE);
        //Client client = new Client(InetAddress.getByName("192.168.1.71"));
        Client client = new Client(InetAddress.getLocalHost());
        
    }
    
    
    

    @Override
    public void actionPerformed(ActionEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}

