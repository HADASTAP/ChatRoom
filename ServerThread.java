import java.io.*;
import java.net.Socket;

public class ServerThread extends Thread {
    private final Socket socket;
    private PrintWriter out;
    private String username;

    public ServerThread(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            String joinMessage = in.readLine();
            if (joinMessage != null && joinMessage.startsWith("JOIN:")) {
                username = joinMessage.substring(5);
                Server.addClient(username, this);
                sendMessage("USER_LIST:" + String.join(",", Server.clients.keySet()));
                Server.broadcast("NEW_USER:" + username);
            } else {
                socket.close();
                return;
            }

            String message;
            while ((message = in.readLine()) != null) {
                if (message.startsWith("MESSAGE:")) {
                    Server.broadcast("MESSAGE:" + username + ":" + message.substring(8));
                } else if (message.equals("LEAVE:")) {
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (username != null) {
                Server.removeClient(username);
                Server.broadcast("USER_LEFT:" + username);
            }
            try {
                socket.close();
            } catch (IOException ignored) {}
        }
    }

    public void sendMessage(String message) {
        out.println(message);
    }
}
