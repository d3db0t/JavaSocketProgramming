import java.io.*;

public class ResponseObject implements Serializable {
    public String httpreq;
    public Object file;
    public String format;
    public String filename;

    public ResponseObject(String req, Object file, String format, String filename)
    {
        this.httpreq = req;
        this.file = file;
        this.format = format;
        this.filename = filename;
    }
    public ResponseObject(String req) // 404
    {
        this.httpreq = req;
    }
    public String getReq()
    {
        return this.httpreq;
    }
    public Object getFile()
    {
        return this.file;
    }
    public String getFormat()
    {
        return this.format;
    }
    public String getFilename()
    {
        return this.filename;
    }
}