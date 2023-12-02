package northeastern.cs5520fa23.greenthumbs.viewmodel.Messages.Chat;

public class ChatMessage {
    private String senderId;
    private String receiverId;
    private String userId;
    private String timestamp;
    private String messageContent;
    private String chatId;
    public static final int Received_View = 0;
    public static final int Sent_View = 1;
    private int viewType;

    public ChatMessage() {}

    public ChatMessage(String senderId, String receiverId, String userId, String timestamp, String messageContent, String chatId) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.userId = userId;
        this.timestamp = timestamp;
        this.messageContent = messageContent;
        this.chatId = chatId;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
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

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }
}
