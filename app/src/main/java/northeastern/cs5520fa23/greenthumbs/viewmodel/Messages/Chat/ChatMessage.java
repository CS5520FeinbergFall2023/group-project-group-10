package northeastern.cs5520fa23.greenthumbs.viewmodel.Messages.Chat;

public class ChatMessage {
    private int senderId;
    private int receiverId;
    private int userId;
    private String timestamp;
    private String messageContent;
    private String chatID;

    public ChatMessage(int senderId, int receiverId, int userId, String timestamp, String messageContent, String chatID) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.userId = userId;
        this.timestamp = timestamp;
        this.messageContent = messageContent;
        this.chatID = chatID;
    }

    public int getSenderId() {
        return senderId;
    }

    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }

    public int getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(int receiverId) {
        this.receiverId = receiverId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public String getChatID() {
        return chatID;
    }

    public void setChatID(String chatID) {
        this.chatID = chatID;
    }
}
