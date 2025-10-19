import java.io.BufferedReader;
import java.io.IOException;

public class ClientThread implements Runnable {
    private final BufferedReader in;

    public ClientThread(BufferedReader in) {
        this.in = in;
    }

    @Override
    public void run() {
        try {
            String message;
            while ((message = in.readLine()) != null) {
                System.out.println("Server: " + message);
            }
        } catch (IOException ignored) {}
    }
}
