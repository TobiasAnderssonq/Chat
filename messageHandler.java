

package ChatIT;

import java.io.IOException;
import java.io.ObjectInputStream;
import javax.swing.JTextArea;

public class messageHandler extends Thread{
    
    private ObjectInputStream in;
    private JTextArea text;
    
    public messageHandler(ObjectInputStream in, JTextArea text) {
        this.in = in;
        this.text = text;
    }   
    
    
    public void run() {
        while(true) {
            printMessages();
        }
    }
    
    private void printMessages(){
     String message = "";
        try {
             message = (String) in.readObject();
            } catch (IOException ex) {
                System.out.println("IO: " + ex);
            } catch (ClassNotFoundException ex) {
                System.out.println("Wont happen for Strings...");
            }
            if(!message.equals("") && !message.equals("\n")) text.append(message + "\n");
            
    }
}