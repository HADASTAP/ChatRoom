import javafx.application.Platform;
import javafx.scene.control.*;

public class Controller {
    private final Client client;
    private final TextArea chatArea;
    private final ListView<String> userListView;
    private final TextField messageField;

    public Controller(Client client, TextArea chatArea, ListView<String> userListView, TextField messageField) {
        this.client = client;
        this.chatArea = chatArea;
        this.userListView = userListView;
        this.messageField = messageField;
    }

    public void processServerMessage(String message) {
        Platform.runLater(() -> {
            Message msg = Message.parse(message);
            if (msg == null) return;

            switch (msg.getType()) {
                case "NEW_USER":
                    chatArea.appendText(msg.getContent() + " joined.\n");
                    userListView.getItems().add(msg.getContent());
                    break;
                case "USER_LEFT":
                    chatArea.appendText(msg.getContent() + " left.\n");
                    userListView.getItems().remove(msg.getContent());
                    break;
                case "USER_LIST":
                    userListView.getItems().clear();
                    userListView.getItems().addAll(msg.getContent().split(","));
                    break;
                case "MESSAGE":
                    chatArea.appendText(msg.getContent() + "\n");
                    break;
            }
        });
    }

    public void sendMessage() {
        String text = messageField.getText().trim();
        if (!text.isEmpty()) {
            client.sendMessage(text);
            messageField.clear();
        }
    }
}
