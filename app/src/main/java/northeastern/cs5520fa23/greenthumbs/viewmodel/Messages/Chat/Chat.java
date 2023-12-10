package northeastern.cs5520fa23.greenthumbs.viewmodel.Messages.Chat;

public class Chat {
    private String last_message;
    private String other_username;
    private String timestamp;

    public Chat() {
    }

    public Chat(String last_message, String other_username, String timestamp) {
        this.last_message = last_message;
        this.other_username = other_username;
        this.timestamp = timestamp;
    }

    public String getLast_message() {
        return last_message;
    }

    public void setLast_message(String last_message) {
        this.last_message = last_message;
    }

    public String getOther_username() {
        return other_username;
    }

    public void setOther_username(String other_username) {
        this.other_username = other_username;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
