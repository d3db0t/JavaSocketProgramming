import java.io.*;
import java.net.*;
import java.util.*;

public class SConnectThread extends Thread{
    static boolean connected = false;
    static MainServer mainServer;
    SConnectThread(MainServer mainServer)
    {
        this.mainServer = mainServer;
    }

    public void run()
    {
        boolean flag = true;
        while(flag)
        {
            try
            {
                System.out.println("Trying to connect to ServerA...");
                SConnectThread.sleep(5000);
                Socket ServerASocket = new Socket("localhost", 7777); // Socket
                mainServer.serverASocket = ServerASocket;
                flag = false;
            }
            catch(Exception e)
            {
                continue;
            }
            
        }
        System.out.println("[+] Connected to ServerA");
    }
}