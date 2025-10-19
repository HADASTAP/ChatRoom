import java.io.*;
import java.net.Socket;

public class Client {
    private final String hostname;
    private final int port;
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    public Client(String hostname, int port) {
        this.hostname = hostname;
        this.port = port;
    }

    public void connect(String username) throws IOException {
        socket = new Socket(hostname, port);
        out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        out.println("JOIN:" + username);

        new Thread(new ClientThread(in)).start();
    }

    public void sendMessage(String message) {
        out.println("MESSAGE:" + message);
    }

    public void disconnect() {
        out.println("LEAVE:");
        try {
            socket.close();
        } catch (IOException ignored) {}
    }
}
