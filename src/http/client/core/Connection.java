package http.client.core;

import http.client.packets.Content;
import http.client.packets.Header;
import http.client.packets.Request;
import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

/**
* Une connexion permet de faire une requête vers le serveur et attend la réponse.
*/
public class Connection extends Observable implements Runnable {

    public static final int MAX_SIZE = 2048;
    private final String address;
    private final int port;
    private final String file;

    private Header header;
    private Content content;

    public Header getHeader() {
        return header;
    }

    public Content getContent() {
        return content;
    }

    public Connection(String address, int port, String file) {
        this.address = address;
        this.port = port;
        this.file = file;
        header = null;
        content = new Content();
    }

    @Override
    public void run() {
        try {
            // Création du socket.
            Socket socket = new Socket(InetAddress.getByName(address), port);
            OutputStream out = socket.getOutputStream();
            // Création de la requête.
            Request r = new Request("GET", file, "HTTP/1.1", address);
            // Envoi de la requête.
            out.write(r.toByteArray());
            out.flush();

            // Buffer de lecture.
            BufferedInputStream inBuf = new BufferedInputStream(socket.getInputStream());
            byte[] data = new byte[MAX_SIZE];
            int length;
            List<Byte> bytes = new ArrayList<>();
            // Lecture de la réponse. Les octets sont placés un par un dans une liste.
            while ((length = inBuf.read(data)) != -1) {
                for (int i = 0; i < length; i++) {
                    bytes.add(data[i]);
                }
                if (length != MAX_SIZE) {
                    break;
                }
            }

            List<Byte> bytesHeader = new ArrayList<>();
            int i = 0;
            for (; i < bytes.size() - 1; i++) {
                if (bytes.get(i) == '\n' && bytes.get(i + 1) == '\n') {
                    break;
                }
                bytesHeader.add(bytes.get(i));
            }
            // Création de l'entête de la réponse.
            header = new Header(bytesHeader);
            i++;
            i++;
            for (; i < bytes.size(); i++) {
                content.addByte(bytes.get(i));
            }

            // Ecriture du fichier reçu.
            if (header.getCode() != 404) {
                FileOutputStream fos = new FileOutputStream("content/" + this.file);
                fos.write(content.getContent());
                fos.close();
            }

        } catch (IOException ex) {
            //Logger.getLogger(Connection.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            setChanged();
            notifyObservers();
        }
    }
}
