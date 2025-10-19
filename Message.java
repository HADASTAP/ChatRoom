public class Message {
    private final String type;
    private final String content;

    public Message(String type, String content) {
        this.type = type;
        this.content = content;
    }

    public String getType() {
        return type;
    }

    public String getContent() {
        return content;
    }

    @Override
    public String toString() {
        return type + ":" + content;
    }

    public static Message parse(String rawMessage) {
        int index = rawMessage.indexOf(":");
        if (index == -1) return null;
        return new Message(rawMessage.substring(0, index), rawMessage.substring(index + 1));
    }
}
