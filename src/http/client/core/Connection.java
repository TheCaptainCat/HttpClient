package http.client.core;

import http.client.packets.Request;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Observable;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Connection extends Observable implements Runnable {
    public static final int MAX_SIZE = 2048;
    private final String address;
    private final int port;
    private final String file;
    
    private String header;
    private String content;

    public String getHeader() {
        return header;
    }

    public String getContent() {
        return content;
    }

    public Connection(String address, int port, String file) {
        this.address = address;
        this.port = port;
        this.file = file;
        header = null;
        content = null;
    }

    @Override
    public void run() {
        try {
            Socket socket = new Socket(InetAddress.getByName(address), port);
            OutputStream out = socket.getOutputStream();
            Request r = new Request("GET", file, "HTTP/1.1", address);
            out.write(r.toByteArray());
            out.flush();
            
            BufferedInputStream inBuf = new BufferedInputStream(socket.getInputStream());
            byte[] data = new byte[MAX_SIZE];
            int length = 0;
            String content = "";
            while ((length = inBuf.read(data)) != -1) {
                content += new String(data, 0, length);
                if (length != MAX_SIZE)
                    break;
            }
            this.header = content.split("\n\n")[0];
            this.content = content.split("\n\n")[1];
            setChanged();
            notifyObservers();
        } catch (IOException ex) {
            Logger.getLogger(Connection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
