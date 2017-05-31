package http.client.core;

import java.util.Observable;
import java.util.Observer;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Client extends Observable implements Runnable, Observer {

    private Queue<Connection> connections;

    public Client() {
        this.connections = new ConcurrentLinkedQueue<>();
    }

    public synchronized void addConnection(Connection c) {
        c.addObserver(this);
        connections.add(c);
        notify();
    }

    @Override
    public synchronized void run() {
        try {
            while (true) {
                while (!connections.isEmpty()) {
                    Connection c = connections.poll();
                    System.out.println("AVANT RUN");
                    new Thread(c).start();
                }
                System.out.println("AVANT WAIT");
                wait();
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        setChanged();
        notifyObservers(o);
    }
}
