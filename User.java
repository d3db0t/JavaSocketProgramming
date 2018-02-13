import java.io.*;
import java.net.*;
import java.util.*;

public class User implements Serializable{
    String username;

    User(String username)
    {
        this.username = username;
    }

    public String getUsername()
    {
        return this.username;
    }
}