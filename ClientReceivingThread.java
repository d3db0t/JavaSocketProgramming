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
                ResponseObject serverResponse = (ResponseObject) ois.readObject();
                char statuscode = serverResponse.getReq().charAt(0);
                if (statuscode == '4')
                {
                    System.out.println(serverResponse.getReq());
                    System.out.println("File not found!");
                }
                else if (serverResponse.getFormat().equals("txt")) // text file
                {
                    String text = (String) serverResponse.getFile();
                    PrintWriter pw = new PrintWriter("userfiles/" + serverResponse.getFilename());
                    pw.println(text);
                    pw.close();
                }
                /*
                else if (serverResponse.getFormat().equals("jpeg") || serverResponse.getFormat().equals("png"))
                {

                }
                */
                // System.out.println(serverResponse);
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