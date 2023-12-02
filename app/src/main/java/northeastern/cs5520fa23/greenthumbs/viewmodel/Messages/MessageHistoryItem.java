package northeastern.cs5520fa23.greenthumbs.viewmodel.Messages;

public class MessageHistoryItem {
    private String other_username;
    private String last_message;
    private String timestamp;
    public MessageHistoryItem() {}
    public MessageHistoryItem(String other_username, String last_message, String timestamp) {
        this.other_username = other_username;
        this.last_message = last_message;
        this.timestamp = timestamp;
    }

    public String getOther_username() {
        return other_username;
    }

    public void setOther_username(String other_username) {
        this.other_username = other_username;
    }

    public String getLast_message() {
        return last_message;
    }

    public void setLast_message(String last_message) {
        this.last_message = last_message;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
