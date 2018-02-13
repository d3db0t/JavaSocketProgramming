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

        
        while(flag)
        {

          ois = new ObjectInputStream(clientSocket.getInputStream());

          serverResponse = (String) ois.readObject();

          System.out.print(serverResponse);

          BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));

          sentence = inFromUser.readLine();

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
