import java.io.*;
import java.net.*;
import java.util.*;

public class MainServer {
  public static final int PORT            = 6666;
  public static final int SERVERAPORT     = 7777;
  public static ArrayList<User> users     = new ArrayList<User>();
  public static ArrayList<Socket> sockets = new ArrayList<Socket>();
  public static Socket serverASocket;

  MainServer(){}

  public static void main(String argv[]) throws Exception
    {
      new MainServer().runServer();
    }

    public void runServer() throws IOException
    {
      ServerSocket welcomeSocket = new ServerSocket(PORT);
      System.out.println("MainServer is up and running...");
      new SConnectThread(this).start();
      while(true)
      {
        Socket socket = welcomeSocket.accept();

        try
        {
          new MainServerThread(socket).start();
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
      return users;
    }

    public ArrayList<Socket> getSockets()
    {
      return sockets;
    }

    public void removeOnlineUser(String username)
    {
      for (int i = 0; i < users.size();i++)
      {
        if (username.equals(users.get(i).getUsername()))
        {
          users.remove(i);
          sockets.remove(i);
        }
      }
    }

    public int getUserSocket(String username)
    {
      int index = -1; // No user by default
      for (int i = 0; i < users.size();i++)
      {
        if (users.get(i).getUsername().equals(username))
        {
          index = i;
          break;
        }
      }

      return index;
    }
  }
