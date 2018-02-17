import java.io.*;
import java.net.*;
import java.util.*;

public class ClientReceivingThread extends Thread{
    Socket socket;
    Boolean flag = true;
    ObjectInputStream ois;

    ClientReceivingThread(Socket socket)
    {
        this.socket = socket;
    }

    public void run()
    {
        try
        {
            while(flag)
            {
                this.ois   = new ObjectInputStream(socket.getInputStream());
                String serverResponse = (String) ois.readObject();
                System.out.println(serverResponse);
                System.out.print(">> ");
                //this.ois.reset();
            }
        }
        catch(Exception e)
        {
            System.out.println("[-] Receiving Thread Failed!");
        }
    }
}