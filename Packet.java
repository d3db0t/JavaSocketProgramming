import java.io.*;
import java.net.*;
import java.util.*;

public class Packet {
    String src;
    String dst;
    String msg;
    int ttl;

    Packet(String src, String dst, String msg, int ttl)
    {
        this.src = src;
        this.dst = dst;
        this.msg = msg;
        this.ttl = ttl;
    }
}