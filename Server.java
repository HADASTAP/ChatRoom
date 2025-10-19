import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

public class Server {
    private static final int PORT = 12345;
    static final ConcurrentHashMap<String, ServerThread> clients = new ConcurrentHashMap<>();

    public static void main(String[] args) {
        System.out.println("Chat server is running on port " + PORT);
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                ServerThread clientThread = new ServerThread(clientSocket);
                clientThread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void broadcast(String message) {
        for (ServerThread client : clients.values()) {
            client.sendMessage(message);
        }
    }

    public static void addClient(String username, ServerThread clientThread) {
        clients.put(username, clientThread);
        broadcastUserList();
    }

    public static void removeClient(String username) {
        clients.remove(username);
        broadcastUserList();
    }

    public static void broadcastUserList() {
        String userList = String.join(",", clients.keySet());
        broadcast("USER_LIST:" + userList);
    }
}
