package http.client.core;

import java.util.Observable;
import java.util.Observer;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
* Le client est concurent. Il permet d'envoyer des requêtes vers le serveur.
*/
public class Client extends Observable implements Runnable, Observer {
    // La file des connexions, remplie par l'interface.
    private Queue<Connection> connections;

    public Client() {
        this.connections = new ConcurrentLinkedQueue<>();
    }

    public synchronized void addConnection(Connection c) {
        c.addObserver(this);
        // Ajout de la connexion.
        connections.add(c);
        // Réveil du client.
        notify();
    }

    @Override
    public synchronized void run() {
        try {
            while (true) {
                // Tant qu'il y a des connexion en attente, elles sont envoyées.
                while (!connections.isEmpty()) {
                    Connection c = connections.poll();
                    // La connexion démarre.
                    new Thread(c).start();
                }
                // Attente du prochain réveil.
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
