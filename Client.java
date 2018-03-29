import java.io.*;
import java.net.*;
import java.util.*;

class Client {
  public static final int PORT = 6666;
  static ObjectInputStream ois;
  static ObjectOutputStream oos;

    public static void main(String argv[]) throws Exception
    {
        String username = argv[0];
        String sentence;
        Object serverResponse;
        Boolean flag = true;

        User user = new User(username);

        Socket clientSocket = new Socket("localhost", PORT); // Socket

        oos = new ObjectOutputStream(clientSocket.getOutputStream()); // Socket object + User object

        oos.writeObject(user); // Send User object through socket

        oos.flush();
        //oos.reset();

        // ClientReceivingThread
        try
        {
          new ClientReceivingThread(clientSocket).start();
        }
        catch(Exception e)
        {
          System.out.println("[-] Receiving Thread Failed!");
        }

        
        while(flag)
        {
          System.out.print(">> ");

          BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));

          sentence = inFromUser.readLine();
          String method = sentence.split(" ")[0]; // method (GET)
          //System.out.println(method);
          String file = sentence.split(" ")[1]; // File name + format
          //System.out.println(file);
          String format = file.split("\\.")[1]; // format
          //System.out.println(format);
          
          sentence = method + " /" + file + " /1.1\n" + "Host: localhost\n"
          + "Accepted format: " + format + "\n" + "Connection: keep-alive";
          System.out.println(sentence);

          if(sentence.equals("Exit"))
          {
            oos = new ObjectOutputStream(clientSocket.getOutputStream());
            oos.writeObject("ClientExitCommand");
            System.out.println("Exiting...");
            flag = false;
          }
          else
          {
            oos = new ObjectOutputStream(clientSocket.getOutputStream()); 
            oos.writeObject(sentence);
          }
      }

      

    }
}
