import java.io.*;
import java.net.*;
import java.util.*;

public class Server {
  public static final int PORT            = 6666;
  public static ArrayList<User> users     = new ArrayList<User>();
  public static ArrayList<Socket> sockets = new ArrayList<Socket>();

  Server(){}

  public static void main(String argv[]) throws Exception
    {
      new Server().runServer();
    }

    public void runServer() throws IOException
    {
      ServerSocket welcomeSocket = new ServerSocket(PORT);
      System.out.println("Server is up and running...");
      while(true)
      {
        Socket socket = welcomeSocket.accept();

        try
        {
          new Sthread(socket).start();
        }
        catch(Exception e)
        {
          continue;
        }
    
      }
    }

    private static String mytoString(ArrayList<String> theArray, String delimiter) 
    {
      StringBuilder sb = new StringBuilder();
      for (int i = 0; i < theArray.size(); i++) {
         if (i > 0) {
            sb.append(delimiter);
         }
         String item = theArray.get(i);
         sb.append(item);
      }
      return sb.toString();
   }

    public void sendOnlineUsers(ObjectOutputStream oos, String username) throws Exception
    {
      ArrayList<String> a = new ArrayList<String>();
      int numOfUsers      = -1;
      for (int i = 0; i < users.size();i++)
      {
        if (!username.equals(users.get(i).getUsername()))
        {
          a.add(users.get(i).getUsername());
        }
        numOfUsers++;
      }
      if(numOfUsers == 0)
      {
        oos.writeObject("Online Users: " + numOfUsers + "\n" + "You are the only one :)");
      }
      else
      {
        oos.writeObject("Online Users:" + numOfUsers + "\n" + mytoString(a, "\n"));
      }
    }

    public ArrayList<User> getUsers()
    {
      return this.users;
    }

    public ArrayList<Socket> getSockets()
    {
      return this.sockets;
    }

    public void removeOnlineUser(String username)
    {
      for (int i = 0; i < this.users.size();i++)
      {
        if (username.equals(this.users.get(i).getUsername()))
        {
          this.users.remove(i);
        }
      }
    }
  }
