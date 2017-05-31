package http.client.core;

import java.net.InetAddress;

public class Connection implements Runnable {
    InetAddress address;
    int port;
    String file;

    public Connection(InetAddress address, int port, String file) {
        this.address = address;
        this.port = port;
        this.file = file;
    }

    @Override
    public void run() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
