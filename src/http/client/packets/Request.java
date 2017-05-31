package http.client.packets;

public class Request {
    private final String method;
    private final String filename;
    private final String version;
    private final String hostname;

    public Request(String method, String filename, String version, String hostname) {
        this.method = method;
        this.filename = filename;
        this.version = version;
        this.hostname = hostname;
    }
    
    public byte[] toByteArray() {
        String s = String.format("%s %s %s\nHost: %s", method, filename, version, hostname);
        return s.getBytes();
    }
}
