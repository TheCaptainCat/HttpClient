package http.client.packets;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
* Cette classe représente une requête HTTP.
*/
public class Request {

    private final String method;
    private final String filename;
    private final String version;
    private final String hostname;
    private String cookie;

    public Request(String method, String filename, String version, String hostname) {
        this.method = method;
        this.filename = filename;
        this.version = version;
        this.hostname = hostname;
        this.cookie = "";

        // Lecture du contenu du dossier "cookies".
        // Si des cookies sont trouvés, ils sont envoyés avec la requête.
        File folder = new File("cookies");
        File[] listOfFiles = folder.listFiles();
        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                if (listOfFiles[i].getName().equals(hostname)) {
                    try (BufferedReader br = new BufferedReader(new FileReader("cookies/" + listOfFiles[i].getName()))) {
                        String line;
                        while ((line = br.readLine()) != null) {
                            this.cookie += line + "; ";
                        }
                    } catch (IOException e) {

                    }
                }
            }
        }
        if (!cookie.equals(""))
            cookie = cookie.substring(0, cookie.length() - 2);
    }

    public byte[] toByteArray() {
        String s = String.format("%s %s %s\nHost: %s\n", method, filename, version, hostname);
        if (!cookie.equals("")) {
            s += "Cookie: " + cookie + "\n";
        }
        return s.getBytes();
    }
}
