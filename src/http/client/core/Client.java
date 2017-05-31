package http.client.core;

public class Client implements Runnable {
    @Override
    public void run() {
        Connection c = new Connection("134.214.227.137", 80, "/index.html");
        c.run();
    }
}
