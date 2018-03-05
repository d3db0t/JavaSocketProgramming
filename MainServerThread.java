import java.io.*;
import java.net.*;
import java.util.*;

public class MainServerThread extends Thread{
  Socket socket;
  User threaduser;
  static MainServer server = new MainServer();
  Boolean flag = true;
  ObjectInputStream ois;
  ObjectOutputStream oos;

  MainServerThread(Socket socket)
  {
    this.socket = socket;
  }

  public void run()
  {
    Object clientSentence;

    try
    {
      this.ois        = new ObjectInputStream(socket.getInputStream());
      User user       = (User) ois.readObject();
      this.threaduser = user;
      System.out.println(user.getUsername() + " Connected");
      server.getUsers().add(user);
      server.getSockets().add(this.socket);

      this.oos = new ObjectOutputStream(socket.getOutputStream());
      server.sendOnlineUsers(oos, this.threaduser.getUsername());
    }
    catch(Exception e)
    {
      flag = false;
      System.out.println("Can not get user info!");
    }

    try
    {
      
      while(flag)
      {

        this.ois = new ObjectInputStream(socket.getInputStream());

        clientSentence = (String) ois.readObject();
        System.out.println(this.threaduser.getUsername() + " wrote " + clientSentence);
        String clientString = (String) clientSentence;

        if (clientString.charAt(0) == '@') // User to chat with
        {
          String[] usermsg      = clientString.split(" "); // Usertochatwith + msg
          String usertochatwith = usermsg[0].substring(1); // Username
          String msg            = usermsg[1];
          int socketIndex       = server.getUserSocket(usertochatwith);
          if (socketIndex == -1)
          {
            this.oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject("[-] Error: Unable to find user"); 
          }
          else
          {
            Socket s = server.getSockets().get(socketIndex);
            this.oos = new ObjectOutputStream(s.getOutputStream());
            oos.writeObject(this.threaduser.getUsername() + ": " + msg); 
          }
        }

        if (clientSentence.equals("ClientExitCommand"))
        {
          flag = false;
          server.removeOnlineUser(this.threaduser.getUsername());
          socket.close();
        }

        if (clientSentence.equals("listusers"))
        {
          this.oos = new ObjectOutputStream(socket.getOutputStream());
          server.sendOnlineUsers(oos, this.threaduser.getUsername());
        }
        
        
      }
    }
    catch(Exception e)
    {
      flag = false;
      System.out.println(threaduser.getUsername() + " Disconnected");
      System.out.print(e);
    }

  }

    
}