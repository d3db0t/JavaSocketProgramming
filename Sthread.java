import java.awt.image.BufferedImage;
import java.io.*;
import java.net.*;
import java.text.*;
import java.util.*;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class Sthread extends Thread{
  Socket socket;
  User threaduser;
  static Server server = new Server();
  Boolean flag = true;
  ObjectInputStream ois;
  ObjectOutputStream oos;
  static File rootdir = new File("docroot");
  static String[] allfiles = rootdir.list();

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

      //this.oos = new ObjectOutputStream(socket.getOutputStream());
      //this.server.sendOnlineUsers(oos, this.threaduser.getUsername());
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
        System.out.println(this.threaduser.getUsername() + " Sent:\n" + clientSentence);
        String clientString = (String) clientSentence;

        Queue<String> queue = new LinkedList<>();
        queue.add(clientString);

        String filename = clientString.split(" ")[1]; // File name + format
        String format = filename.split("\\.")[1]; // format
        
        filename = filename.substring(1, filename.length());
        System.out.println(filename);

        boolean foundfile = false;

        for(int j = 0; j <allfiles.length; j++){
            if(allfiles.length == 0)
            {
              break;
            }
            if(filename.equals(allfiles[j]))
            {
              foundfile = true;
            }
            
        }
        System.out.println(foundfile);
        DateFormat df = new SimpleDateFormat("yyyy/dd/MM HH:mm:ss");
        Date d = new Date();
        String st = df.format(d);

        if(!foundfile) // File not found 
        {
          ResponseObject ro = new ResponseObject("404 NOT FOUND/1.1\n" + st + "\n" + format + "\nkeep-alive");
          this.oos = new ObjectOutputStream(socket.getOutputStream());
          oos.writeObject(ro);         
        }
        else if (foundfile) // file found
        {
          FileReader filer = new FileReader("docroot/" + filename); // get file
          if (format.equals("txt")) // text file
          {
            BufferedReader br = new BufferedReader(filer);
            String readline;
            String fileLines = "";
            while((readline = br.readLine()) != null)
            {
                fileLines = fileLines + readline + "\n";
            }

            ResponseObject ro = new ResponseObject("200 OK /1.1\n" + st + "\n" + format + "\nkeep-alive", fileLines, format, filename);
            this.oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(ro);

          }

          else if (format.equals("jpeg") || format.equals("png")) // image
          {
            File ff = new File("docroot/" + filename);
            BufferedImage bi = ImageIO.read(ff); // reading file
            ImageIcon ii = new ImageIcon(bi); // parsing image
            ResponseObject ro = new ResponseObject("200 OK /1.1\n" + st + "\n" + format + "\nkeep-alive", ii, format, filename);
            this.oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(ro);
          }
           
        }







        if (clientString.charAt(0) == '@') // User to chat with
        {
          String[] usermsg      = clientString.split(" "); // Usertochatwith + msg
          String usertochatwith = usermsg[0].substring(1); // Username
          String msg            = usermsg[1];
          int socketIndex       = this.server.getUserSocket(usertochatwith);
          if (socketIndex == -1)
          {
            this.oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject("[-] Error: Unable to find user"); 
          }
          else
          {
            Socket s = this.server.getSockets().get(socketIndex);
            this.oos = new ObjectOutputStream(s.getOutputStream());
            oos.writeObject(this.threaduser.getUsername() + ": " + msg); 
          }
        }

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
      System.out.print(e);
    }

  }

    
}