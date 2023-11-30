package northeastern.cs5520fa23.greenthumbs.viewmodel.Messages;

public class MessageHistoryItem {
    private String username;
    private String lastMessage;
    public MessageHistoryItem(String username, String lastMessage) {
        this.username = username;
        this.lastMessage = lastMessage;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }
}
