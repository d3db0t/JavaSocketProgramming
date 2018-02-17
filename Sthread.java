import java.io.*;
import java.net.*;
import java.util.*;

public class Sthread extends Thread{
  Socket socket;
  User threaduser;
  static Server server = new Server();
  Boolean flag = true;
  ObjectInputStream ois;
  ObjectOutputStream oos;

  Sthread(Socket socket)
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
      this.server.getUsers().add(user);
      this.server.getSockets().add(this.socket);

      this.oos = new ObjectOutputStream(socket.getOutputStream());
      this.server.sendOnlineUsers(oos, this.threaduser.getUsername());
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

        
        if (clientSentence.equals("ClientExitCommand"))
        {
          flag = false;
          this.server.removeOnlineUser(this.threaduser.getUsername());
          socket.close();
        }

        if (clientSentence.equals("listusers"))
        {
          this.oos = new ObjectOutputStream(socket.getOutputStream());
          this.server.sendOnlineUsers(oos, this.threaduser.getUsername());
        }
        
        
      }
    }
    catch(Exception e)
    {
      flag = false;
      System.out.println(threaduser.getUsername() + " Disconnected");
    }

  }

    
}